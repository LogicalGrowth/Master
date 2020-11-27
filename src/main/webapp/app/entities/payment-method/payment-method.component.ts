import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import { PaymentMethodService } from './payment-method.service';
import { PaymentMethodDeleteDialogComponent } from './payment-method-delete-dialog.component';
import { User } from 'app/core/user/user.model';
import { AccountService } from 'app/core/auth/account.service';
import { ApplicationUserService } from '../application-user/application-user.service';
import { ApplicationUser, IApplicationUser } from 'app/shared/model/application-user.model';
@Component({
  selector: 'jhi-payment-method',
  templateUrl: './payment-method.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss', './payment-method.scss'],
})
export class PaymentMethodComponent implements OnInit, OnDestroy {
  paymentMethods?: IPaymentMethod[];
  eventSubscriber?: Subscription;
  data?: IPaymentMethod[];
  account!: User;
  applicationUser?: IApplicationUser[];

  constructor(
    protected paymentMethodService: PaymentMethodService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    private accountService: AccountService,
    private applicationUserService: ApplicationUserService
  ) {}

  loadAll(): void {
    this.paymentMethodService.query().subscribe((res: HttpResponse<IPaymentMethod[]>) => (this.paymentMethods = res.body || []));
  }

  loadPaymentMethods(): void {
    if (this.applicationUser != null) {
      this.paymentMethodService.query({ 'ownerId.equals': this.applicationUser[0].id }).subscribe((res: HttpResponse<IPaymentMethod[]>) => {
        this.paymentMethods = res.body || [];
        this.setImageType();
      });
    }
  }

  setImageType(): void {
    if (this.paymentMethods) {
      for (let i = 0; i < this.paymentMethods.length; i++) {
        if (this.paymentMethods[i].type === 'MASTERCARD') {
          this.paymentMethods[i].typeImage = '../../../content/images/CardTypes/Mastercard.png';
        } else if (this.paymentMethods[i].type === 'VISA') {
          this.paymentMethods[i].typeImage = '../../../content/images/CardTypes/Visa.png';
        } else if (this.paymentMethods[i].type === 'EXPRESS') {
          this.paymentMethods[i].typeImage = '../../../content/images/CardTypes/Express.png';
        }
      }
    }
  }

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      if (account) {
        this.account = account;

        this.applicationUserService
          .query({ 'internalUserId.equals': this.account.id })
          .subscribe((res: HttpResponse<IApplicationUser[]>) => {
            this.applicationUser = res.body || [];
            this.loadPaymentMethods();
          });
      }
    });

    this.registerChangeInPaymentMethods();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPaymentMethod): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPaymentMethods(): void {
    this.eventSubscriber = this.eventManager.subscribe('paymentMethodListModification', () => this.loadPaymentMethods());
  }

  delete(paymentMethod: IPaymentMethod): void {
    const modalRef = this.modalService.open(PaymentMethodDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.paymentMethod = paymentMethod;
  }

  like(e: any): void {
    const hearts = document.getElementsByClassName('like');

    for (let i = 0; i < hearts.length; i++) {
      hearts[i].classList.remove('heart');
    }
    e.currentTarget.classList.add('heart');

    const id = e.currentTarget.id;
    this.searchPaymentMethod(id);
  }

  searchPaymentMethod(id: any): void {
    this.paymentMethods?.forEach(e => {
      if (e.favorite === true) {
        e.favorite = false;
        this.subscribeToSaveResponse(this.paymentMethodService.update(e));
      }
    });
    this.paymentMethodService.find(parseInt(id, 10)).subscribe(e => {
      const newFavoritePaymentMethod = e.body;
      if (newFavoritePaymentMethod) {
        newFavoritePaymentMethod.favorite = true;
        newFavoritePaymentMethod.owner = { id: this.applicationUser![0].id };
        this.subscribeToSaveResponse(this.paymentMethodService.update(newFavoritePaymentMethod));
        this.loadPaymentMethods();
      }
    });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaymentMethod>>): void {
    result.subscribe();
  }
}
