import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAuction } from 'app/shared/model/auction.model';
import { AuctionService } from './auction.service';

@Component({
  templateUrl: './auction-delete-dialog.component.html',
})
export class AuctionDeleteDialogComponent {
  auction?: IAuction;

  constructor(protected auctionService: AuctionService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.auctionService.delete(id).subscribe(() => {
      this.eventManager.broadcast('auctionListModification');
      this.activeModal.close();
    });
  }
}
