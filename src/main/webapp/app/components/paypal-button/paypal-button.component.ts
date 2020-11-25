import { Component, OnInit, ViewChild, ElementRef, Input } from '@angular/core';
import { PaymentService } from 'app/entities/payment/payment.service';

import { IPayment, Payment } from 'app/shared/model/payment.model';
declare let paypal: any;
import { ProductType } from 'app/shared/model/enumerations/product-type.model';
import { AccountService } from 'app/core/auth/account.service';
import { User } from 'app/core/user/user.model';
import * as moment from 'moment';
import { IProyect } from '../../shared/model/proyect.model';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PaymentMethodSelectDialogComponent } from 'app/entities/payment-method/payment-method-select-dialog';
import { IPaymentMethod } from 'app/shared/model/payment-method.model';
@Component({
  selector: 'jhi-paypal-button',
  templateUrl: './paypal-button.component.html',
  styleUrls: ['./paypal-button.component.scss'],
})
export class PaypalButtonComponent implements OnInit {
  @ViewChild('paypal', { static: true }) paypalElement: ElementRef;

  @Input() description: string;
  @Input() price: string;
  @Input() projectId: string;
  @Input() productType?: ProductType;
  finalPrice = 0;
  paidFor = false;
  idUser = '';
  account!: User;
  projectIdFormat?: number;
  productTypeFormat?: IPayment;

  constructor(
    paypalElement: ElementRef,
    protected paymentService: PaymentService,
    private accountService: AccountService,
    protected modalService: NgbModal
  ) {
    // Initialization inside the constructor
    this.paypalElement = paypalElement;
    this.description = '';
    this.price = '';
    this.projectId = '';
  }

  // tslint:disable-next-line: typedef
  ngOnInit() {
    this.finalPrice = +this.price.replace(/,/, '.');
    this.projectIdFormat = parseInt(this.projectId, 10);
    this.accountService.identity().subscribe(account => {
      if (account) {
        this.account = account;
      }
    });
    paypal
      .Buttons({
        style: {
          layout: 'horizontal',
        },
        createOrder: (data: any, actions: any) => {
          return actions.order.create({
            // eslint-disable-next-line @typescript-eslint/camelcase
            purchase_units: [
              {
                description: this.description,
                amount: {
                  currencyCode: 'USD',
                  value: this.finalPrice,
                },
              },
            ],
          });
        },
        onApprove: async (data: any, actions: { order: { capture: () => any } }) => {
          const order = await actions.order.capture();
          this.paidFor = true;
          // eslint-disable-next-line @typescript-eslint/camelcase
          const payment = this.createPayment(order.create_time);
          this.subscribeToSaveResponse(this.paymentService.createWithoutFormat(payment));
        },
      })
      .render(this.paypalElement.nativeElement);
  }
  // eslint-disable-next-line @typescript-eslint/camelcase
  private createPayment(create_time: any): IPayment {
    return {
      ...new Payment(),
      amount: this.finalPrice,
      applicationUser: this.account,
      proyect: {
        id: this.projectIdFormat,
      },
      type: this.productType,
      // eslint-disable-next-line @typescript-eslint/camelcase
      timeStamp: create_time,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPayment>>): void {
    result.subscribe();
  }

  paymentMethods(): void {
    const modalRef = this.modalService.open(PaymentMethodSelectDialogComponent, { size: 'lg', backdrop: 'static' });
    const payment = this.createPayment(moment());
    modalRef.componentInstance.payment = payment;
    modalRef.componentInstance.description = this.description;
  }
}
