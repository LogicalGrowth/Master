import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrize } from 'app/shared/model/prize.model';
import { PrizeService } from './prize.service';
import { PrizeDeleteDialogComponent } from './prize-delete-dialog.component';

@Component({
  selector: 'jhi-prize',
  templateUrl: './prize.component.html',
})
export class PrizeComponent implements OnInit, OnDestroy {
  prizes?: IPrize[];
  eventSubscriber?: Subscription;

  constructor(protected prizeService: PrizeService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.prizeService.query().subscribe((res: HttpResponse<IPrize[]>) => (this.prizes = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPrizes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPrize): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPrizes(): void {
    this.eventSubscriber = this.eventManager.subscribe('prizeListModification', () => this.loadAll());
  }

  delete(prize: IPrize): void {
    const modalRef = this.modalService.open(PrizeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.prize = prize;
  }
}
