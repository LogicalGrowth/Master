import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import { PaymentMethodService } from './payment-method.service';
import { PaymentMethodDeleteDialogComponent } from './payment-method-delete-dialog.component';

@Component({
  selector: 'jhi-payment-method',
  templateUrl: './payment-method.component.html',
})
export class PaymentMethodComponent implements OnInit, OnDestroy {
  paymentMethods?: IPaymentMethod[];
  eventSubscriber?: Subscription;

  constructor(
    protected paymentMethodService: PaymentMethodService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.paymentMethodService.query().subscribe((res: HttpResponse<IPaymentMethod[]>) => (this.paymentMethods = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
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
    this.eventSubscriber = this.eventManager.subscribe('paymentMethodListModification', () => this.loadAll());
  }

  delete(paymentMethod: IPaymentMethod): void {
    const modalRef = this.modalService.open(PaymentMethodDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.paymentMethod = paymentMethod;
  }
}
