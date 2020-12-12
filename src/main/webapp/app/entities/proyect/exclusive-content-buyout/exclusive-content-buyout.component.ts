import { AfterViewInit, Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ProductType } from 'app/shared/model/enumerations/product-type.model';
import { IExclusiveContent } from 'app/shared/model/exclusive-content.model';

@Component({
  selector: 'jhi-exclusive-content-buyout',
  templateUrl: './exclusive-content-buyout.component.html',
  styleUrls: ['../../../../content/scss/paper-dashboard.scss'],
})
export class ExclusiveContentBuyoutComponent implements AfterViewInit {
  @Input() public exclusiveContent: IExclusiveContent | undefined;

  public amount = 0;
  productType?: ProductType = ProductType.EXCLUSIVE_CONTENT;

  data = {
    currencyCode: 'USD',
    value: this.amount,
  };

  authenticationError = false;

  constructor(public activeModal: NgbActiveModal) {}

  ngAfterViewInit(): void {
    this.data.value = this.exclusiveContent?.price as number;
  }

  setAmount(amount: number): void {
    this.data.value = amount;
  }
}
