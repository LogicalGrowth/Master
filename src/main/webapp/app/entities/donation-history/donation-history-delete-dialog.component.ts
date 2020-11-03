import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDonationHistory } from 'app/shared/model/donation-history.model';
import { DonationHistoryService } from './donation-history.service';

@Component({
  templateUrl: './donation-history-delete-dialog.component.html',
})
export class DonationHistoryDeleteDialogComponent {
  donationHistory?: IDonationHistory;

  constructor(
    protected donationHistoryService: DonationHistoryService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.donationHistoryService.delete(id).subscribe(() => {
      this.eventManager.broadcast('donationHistoryListModification');
      this.activeModal.close();
    });
  }
}
