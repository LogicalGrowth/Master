import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import { PaymentMethodService } from './payment-method.service';
import { AccountService } from 'app/core/auth/account.service';
import { IPayment } from 'app/shared/model/payment.model';
import { User } from 'app/core/user/user.model';
import { HttpResponse } from '@angular/common/http';
import { IProyect } from '../../shared/model/proyect.model';
import { ProyectService } from '../proyect/proyect.service';
import { PaymentService } from '../payment/payment.service';
import { Observable } from 'rxjs';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { ApplicationUserService } from '../application-user/application-user.service';

@Component({
  templateUrl: './payment-method-select-dialog.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss', './payment-method-select-dialog.scss'],
})
export class PaymentMethodSelectDialogComponent implements OnInit {
  paymentMethod?: IPaymentMethod;
  account!: User;
  payment?: IPayment;
  paymentMethods?: IPaymentMethod[];
  proyects?: IProyect[];
  selected = false;
  invoice = false;
  hidePayment = false;
  loading = false;
  number1 = '../../../content/images/cardPayment.png';
  number2 = '../../../content/images/invoice.png';
  name = '';
  description = '';
  proyect: any;
  applicationUser?: IApplicationUser[];

  constructor(
    protected paymentMethodService: PaymentMethodService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager,
    private accountService: AccountService,
    private proyectService: ProyectService,
    protected paymentService: PaymentService,
    protected applicationUserService: ApplicationUserService
  ) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      if (account) {
        this.account = account;
        this.name = account.firstName;
        this.applicationUserService
          .query({ 'internalUserId.equals': this.account.id })
          .subscribe((res: HttpResponse<IApplicationUser[]>) => {
            this.applicationUser = res.body || [];
            this.loadPaymentMethods();
          });
      }
    });

    const paymentInfo = document.querySelector('.modal-body-paymentInfo');
    paymentInfo?.classList.add('hidden');
  }

  loadPaymentMethods(): void {
    if (this.applicationUser != null) {
      this.paymentMethodService.query({ 'ownerId.equals': this.applicationUser[0].id }).subscribe((res: HttpResponse<IPaymentMethod[]>) => {
        this.paymentMethods = res.body || [];
        this.setImageType();
      });
    }

    if (this.payment?.proyect?.id) {
      this.proyectService.find(this.payment.proyect.id).subscribe(e => {
        this.proyect = e.body;
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

        if (this.paymentMethods[i].favorite === true) {
          this.selected = true;
          this.paymentMethod = this.paymentMethods[i];
        }
      }
    }
  }

  cancel(): void {
    this.activeModal.dismiss();
    this.selected = false;
    this.invoice = false;
    this.hidePayment = false;
    this.loading = false;

    const cards = document.getElementsByClassName('paymentCard');
    for (let i = 0; i < cards.length; i++) {
      cards[i].classList.remove('selected');
    }
  }

  addPaymentMethod(e: any): void {
    const cards = document.getElementsByClassName('paymentCard');
    for (let i = 0; i < cards.length; i++) {
      cards[i].classList.remove('selected');
    }
    e.currentTarget.classList.add('selected');
    this.selected = true;

    this.getSelectedCardInfo(e.currentTarget.id);
  }

  getSelectedCardInfo(id: any): void {
    this.paymentMethod = this.paymentMethods?.find(e => e.id === parseInt(id, 10));
  }

  viewInvoice(): void {
    const cards = document.querySelector('.modal-body--paymentMethod');
    cards?.classList.add('hidden');
    this.hidePayment = true;
    this.loading = true;
    const step2 = document.querySelector('.step2');
    step2?.classList.add('stepActive');
    const step1 = document.querySelector('.step1');
    step1?.classList.remove('stepActive');
    setTimeout(() => {
      this.hideLoading();
    }, 2000);
  }

  back(): void {
    const paymentInfo = document.querySelector('.modal-body-paymentInfo');
    paymentInfo?.classList.add('hidden');
    const cards = document.querySelector('.modal-body--paymentMethod');
    cards?.classList.remove('hidden');

    this.invoice = false;
    this.hidePayment = false;

    const step2 = document.querySelector('.step2');
    step2?.classList.remove('stepActive');
    const step1 = document.querySelector('.step1');
    step1?.classList.add('stepActive');
  }

  hideLoading(): void {
    this.invoice = true;
    this.loading = false;
    const paymentInfo = document.querySelector('.modal-body-paymentInfo');
    paymentInfo?.classList.remove('hidden');
  }

  pay(): void {
    if (this.payment) {
      this.payment.proyect = this.proyect;
      this.subscribeToSaveResponse(this.paymentService.createWithoutFormat(this.payment));
    }

    this.cancel();
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPayment>>): void {
    result.subscribe();
  }
}
