import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAuction } from 'app/shared/model/auction.model';
import { AuctionService } from './auction.service';
import { AuctionDeleteDialogComponent } from './auction-delete-dialog.component';

@Component({
  selector: 'jhi-auction',
  templateUrl: './auction.component.html',
})
export class AuctionComponent implements OnInit, OnDestroy {
  auctions?: IAuction[];
  eventSubscriber?: Subscription;

  constructor(protected auctionService: AuctionService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.auctionService.query().subscribe((res: HttpResponse<IAuction[]>) => (this.auctions = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInAuctions();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IAuction): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInAuctions(): void {
    this.eventSubscriber = this.eventManager.subscribe('auctionListModification', () => this.loadAll());
  }

  delete(auction: IAuction): void {
    const modalRef = this.modalService.open(AuctionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.auction = auction;
  }
}
