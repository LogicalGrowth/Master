import { HttpResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { AbstractControl, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { AccountService } from 'app/core/auth/account.service';
import { User } from 'app/core/user/user.model';
import { ApplicationUserService } from 'app/entities/application-user/application-user.service';
import { PaymentMethodService } from 'app/entities/payment-method/payment-method.service';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { Auction, IAuction } from 'app/shared/model/auction.model';
import { IPaymentMethod, PaymentMethod } from 'app/shared/model/payment-method.model';
import { IProyect } from 'app/shared/model/proyect.model';
import { Observable } from 'rxjs';
import { AuctionService } from '../auction.service';
import { BidModalService } from './bidModal.service';

type SelectableEntity = IAuction | IApplicationUser | IProyect;

@Component({
  selector: 'jhi-bid',
  templateUrl: './bid.component.html',
  styleUrls: ['../../../../content/scss/paper-dashboard.scss'],
})
export class BidComponent implements OnInit {
  @Input() public auction: IAuction | undefined;

  isSaving = false;

  editForm = this.fb.group({
    bid: [null, [Validators.required, Validators.min(0), (control: AbstractControl) => Validators.min(this.winningBid + 0.1)(control)]],
  });

  winningBid = 0;
  account!: User;
  applicationUser?: IApplicationUser[];
  favoritePaymentMethod?: IPaymentMethod[];

  constructor(
    public activeModal: NgbActiveModal,
    protected auctionService: AuctionService,
    private accountService: AccountService,
    private applicationUserService: ApplicationUserService,
    private paymentMethodService: PaymentMethodService,
    private bidModalService: BidModalService,
    private router: Router,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    if (this.auction?.winningBid) this.winningBid = this.auction?.winningBid;
    else this.winningBid = this.auction?.initialBid!;

    this.accountService.identity().subscribe(account => {
      if (account) {
        this.account = account;

        this.applicationUserService
          .query({ 'internalUserId.equals': this.account.id })
          .subscribe((res: HttpResponse<IApplicationUser[]>) => {
            this.applicationUser = res.body || [];

            this.getFavorites();
          });
      }
    });
  }

  getFavorites(): void {
    this.paymentMethodService
      .query({ 'favorite.equals': true, 'ownerId.equals': this.applicationUser![0].id })
      .subscribe((res: HttpResponse<PaymentMethod[]>) => {
        this.favoritePaymentMethod = res.body || [];
      });
  }

  save(): void {
    this.isSaving = true;
    const auction = this.createFromForm();

    this.subscribeToSaveResponse(this.auctionService.update(auction));
  }

  private createFromForm(): IAuction {
    return {
      ...new Auction(),
      id: this.auction?.id,
      initialBid: this.auction?.initialBid,
      expirationDate: this.auction?.expirationDate,
      state: this.auction?.state,
      prize: this.auction?.prize,
      proyect: this.auction?.proyect,
      winner: this.applicationUser![0],
      winningBid: this.editForm.get(['bid'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAuction>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.bidModalService.close();
    this.reload();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  reload(): void {
    const currentUrl = this.router.url;
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.router.onSameUrlNavigation = 'reload';
    this.router.navigate([currentUrl]);
  }

  close(): void {
    this.bidModalService.close();
  }
}
