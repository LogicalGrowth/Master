import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICheckpoint } from 'app/shared/model/checkpoint.model';
import { CheckpointService } from './checkpoint.service';

@Component({
  templateUrl: './checkpoint-delete-dialog.component.html',
})
export class CheckpointDeleteDialogComponent {
  checkpoint?: ICheckpoint;

  constructor(
    protected checkpointService: CheckpointService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.checkpointService.delete(id).subscribe(() => {
      this.eventManager.broadcast('checkpointListModification');
      this.activeModal.close();
    });
  }
}
