import { Component, OnInit, ViewChild, ElementRef, Input } from '@angular/core';
import { PaymentService } from 'app/entities/payment/payment.service';

import { IPayment, Payment } from 'app/shared/model/payment.model';
declare let paypal: any;
import { ProductType } from 'app/shared/model/enumerations/product-type.model';
import { AccountService } from 'app/core/auth/account.service';
import { User } from 'app/core/user/user.model';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { ApplicationUserService } from 'app/entities/application-user/application-user.service';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { DonationModalService } from 'app/entities/proyect/donation/donationModal.service';

@Component({
  selector: 'jhi-paypal-button',
  templateUrl: './paypal-button.component.html',
  styleUrls: ['./paypal-button.component.scss'],
})
export class PaypalButtonComponent implements OnInit {
  @ViewChild('paypal', { static: true }) paypalElement: ElementRef;

  @Input() description: string;
  @Input() amount: any | undefined;
  @Input() projectId: string;
  @Input() productType?: ProductType;
  paidFor = false;
  idUser = '';
  account!: User;
  projectIdFormat?: number;
  productTypeFormat?: IPayment;
  applicationUser?: IApplicationUser[];

  constructor(
    paypalElement: ElementRef,
    protected paymentService: PaymentService,
    private accountService: AccountService,
    private applicationUserService: ApplicationUserService,
    private donationModalService: DonationModalService
  ) {
    // Initialization inside the constructor
    this.paypalElement = paypalElement;
    this.description = '';
    this.projectId = '';
  }

  // tslint:disable-next-line: typedef
  ngOnInit() {
    this.projectIdFormat = parseInt(this.projectId, 10);

    this.accountService.identity().subscribe(account => {
      if (account) {
        this.account = account;

        this.applicationUserService
          .query({ 'internalUserId.equals': this.account.id })
          .subscribe((res: HttpResponse<IApplicationUser[]>) => {
            this.applicationUser = res.body || [];
          });
      }
    });

    paypal
      .Buttons({
        createOrder: (data: any, actions: any) => {
          return actions.order.create({
            // eslint-disable-next-line @typescript-eslint/camelcase
            purchase_units: [
              {
                description: this.description,
                amount: this.amount,
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
      amount: this.amount?.value,
      applicationUser: this.applicationUser![0],
      proyect: {
        id: this.projectIdFormat,
      },
      type: this.productType,
      // eslint-disable-next-line @typescript-eslint/camelcase
      timeStamp: create_time,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPayment>>): void {
    result.subscribe(() => {
      this.donationModalService.close();
    });
  }
}
