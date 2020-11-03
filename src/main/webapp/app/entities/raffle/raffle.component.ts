import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRaffle } from 'app/shared/model/raffle.model';
import { RaffleService } from './raffle.service';
import { RaffleDeleteDialogComponent } from './raffle-delete-dialog.component';

@Component({
  selector: 'jhi-raffle',
  templateUrl: './raffle.component.html',
})
export class RaffleComponent implements OnInit, OnDestroy {
  raffles?: IRaffle[];
  eventSubscriber?: Subscription;

  constructor(protected raffleService: RaffleService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.raffleService.query().subscribe((res: HttpResponse<IRaffle[]>) => (this.raffles = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInRaffles();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IRaffle): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInRaffles(): void {
    this.eventSubscriber = this.eventManager.subscribe('raffleListModification', () => this.loadAll());
  }

  delete(raffle: IRaffle): void {
    const modalRef = this.modalService.open(RaffleDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.raffle = raffle;
  }
}
