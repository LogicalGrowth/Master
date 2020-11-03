import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPrize } from 'app/shared/model/prize.model';
import { PrizeService } from './prize.service';

@Component({
  templateUrl: './prize-delete-dialog.component.html',
})
export class PrizeDeleteDialogComponent {
  prize?: IPrize;

  constructor(protected prizeService: PrizeService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.prizeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('prizeListModification');
      this.activeModal.close();
    });
  }
}
