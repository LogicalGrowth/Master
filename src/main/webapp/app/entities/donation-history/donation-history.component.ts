import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDonationHistory } from 'app/shared/model/donation-history.model';
import { DonationHistoryService } from './donation-history.service';
import { DonationHistoryDeleteDialogComponent } from './donation-history-delete-dialog.component';

@Component({
  selector: 'jhi-donation-history',
  templateUrl: './donation-history.component.html',
})
export class DonationHistoryComponent implements OnInit, OnDestroy {
  donationHistories?: IDonationHistory[];
  eventSubscriber?: Subscription;

  constructor(
    protected donationHistoryService: DonationHistoryService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.donationHistoryService.query().subscribe((res: HttpResponse<IDonationHistory[]>) => (this.donationHistories = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInDonationHistories();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IDonationHistory): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInDonationHistories(): void {
    this.eventSubscriber = this.eventManager.subscribe('donationHistoryListModification', () => this.loadAll());
  }

  delete(donationHistory: IDonationHistory): void {
    const modalRef = this.modalService.open(DonationHistoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.donationHistory = donationHistory;
  }
}
