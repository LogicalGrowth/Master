import { Component, AfterViewInit, Input } from '@angular/core';
import { IProyect } from 'app/shared/model/proyect.model';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ProductType } from 'app/shared/model/enumerations/product-type.model';
import { IRaffle } from 'app/shared/model/raffle.model';

@Component({
  selector: 'jhi-ticket',
  templateUrl: './ticket.component.html',
  styleUrls: ['../../../../content/scss/paper-dashboard.scss'],
})
export class TicketComponent implements AfterViewInit {
  @Input() public raffle: IRaffle | undefined;
  @Input() public proyect: IProyect | undefined;
  public amount = 0;
  productType?: ProductType = ProductType.RAFFLE;

  data = {
    currencyCode: 'USD',
    value: this.amount,
  };

  constructor(public activeModal: NgbActiveModal) {}

  ngAfterViewInit(): void {
    this.data.value = this.raffle?.price as number;
  }

  setAmount(amount: number): void {
    this.data.value = amount;
  }
}
