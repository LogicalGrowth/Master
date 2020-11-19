import { Component, OnInit, ViewChild, ElementRef, Input } from '@angular/core';

declare let paypal: any;

@Component({
  selector: 'jhi-paypal-button',
  templateUrl: './paypal-button.component.html',
  styleUrls: ['./paypal-button.component.scss'],
})
export class PaypalButtonComponent implements OnInit {
  @ViewChild('paypal', { static: true }) paypalElement: ElementRef;

  @Input() description: string;
  @Input() price: string;

  finalPrice = 0;
  paidFor = false;

  product = {
    price: 777.77,
    description: 'used couch, decent condition',
    img: 'https://picsum.photos/id/237/200/300',
  };

  constructor(paypalElement: ElementRef) {
    // Initialization inside the constructor
    this.paypalElement = paypalElement;
    this.description = '';
    this.price = '';
  }

  // tslint:disable-next-line: typedef
  ngOnInit() {
    this.finalPrice = +this.price.replace(/,/, '.');

    paypal
      .Buttons({
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
        },
      })
      .render(this.paypalElement.nativeElement);
  }
}
