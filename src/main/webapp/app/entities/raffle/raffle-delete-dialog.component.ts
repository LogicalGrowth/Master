import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRaffle } from 'app/shared/model/raffle.model';
import { RaffleService } from './raffle.service';

@Component({
  templateUrl: './raffle-delete-dialog.component.html',
})
export class RaffleDeleteDialogComponent {
  raffle?: IRaffle;

  constructor(protected raffleService: RaffleService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.raffleService.delete(id).subscribe(() => {
      this.eventManager.broadcast('raffleListModification');
      this.activeModal.close();
    });
  }
}
