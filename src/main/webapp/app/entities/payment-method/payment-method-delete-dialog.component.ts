import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import { PaymentMethodService } from './payment-method.service';

@Component({
  templateUrl: './payment-method-delete-dialog.component.html',
})
export class PaymentMethodDeleteDialogComponent {
  paymentMethod?: IPaymentMethod;

  constructor(
    protected paymentMethodService: PaymentMethodService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

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
