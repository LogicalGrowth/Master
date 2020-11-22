import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import { PaymentMethodService } from './payment-method.service';
import { AccountService } from 'app/core/auth/account.service';
import { IPayment } from 'app/shared/model/payment.model';
import { User } from 'app/core/user/user.model';
import { HttpResponse } from '@angular/common/http';

@Component({
  templateUrl: './payment-method-select-dialog.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss', './payment-method-select-dialog.scss'],
})
export class PaymentMethodSelectDialogComponent implements OnInit {
  paymentMethod?: IPaymentMethod;
  account!: User;
  payment?: IPayment;
  paymentMethods?: IPaymentMethod[];

  constructor(
    protected paymentMethodService: PaymentMethodService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager,
    private accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      if (account) {
        this.account = account;
      }
    });

    this.loadPaymentMethods(this.account.id);
    const paymentInfo = document.querySelector('.modal-body-paymentInfo');
    paymentInfo?.classList.add('hidden');
  }

  loadPaymentMethods(userId: number): void {
    if (userId != null) {
      this.paymentMethodService.query({ 'ownerId.equals': userId }).subscribe((res: HttpResponse<IPaymentMethod[]>) => {
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

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paymentMethodService.delete(id).subscribe(() => {
      this.eventManager.broadcast('paymentMethodListModification');
      this.activeModal.close();
    });
  }
}
