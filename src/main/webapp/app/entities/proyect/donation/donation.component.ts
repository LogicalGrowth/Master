import { Component, AfterViewInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ProductType } from 'app/shared/model/enumerations/product-type.model';
import { IProyect } from 'app/shared/model/proyect.model';

@Component({
  selector: 'jhi-donation',
  templateUrl: './donation.component.html',
  styleUrls: ['../../../../content/scss/paper-dashboard.scss'],
})
export class DonationComponent implements AfterViewInit {
  @Input() public proyect: IProyect | undefined;

  public amount = 0;
  productType?: ProductType = ProductType.DONATION;

  data = {
    currencyCode: 'USD',
    value: this.amount,
  };

  authenticationError = false;

  constructor(public activeModal: NgbActiveModal) {}

  ngAfterViewInit(): void {}

  setAmount(amount: number): void {
    this.data.value = amount;
  }
}
