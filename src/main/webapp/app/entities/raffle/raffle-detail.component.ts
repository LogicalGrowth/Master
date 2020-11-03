import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRaffle } from 'app/shared/model/raffle.model';

@Component({
  selector: 'jhi-raffle-detail',
  templateUrl: './raffle-detail.component.html',
})
export class RaffleDetailComponent implements OnInit {
  raffle: IRaffle | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ raffle }) => (this.raffle = raffle));
  }

  previousState(): void {
    window.history.back();
  }
}
