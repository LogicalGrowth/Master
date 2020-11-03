import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IExclusiveContent } from 'app/shared/model/exclusive-content.model';
import { ExclusiveContentService } from './exclusive-content.service';

@Component({
  templateUrl: './exclusive-content-delete-dialog.component.html',
})
export class ExclusiveContentDeleteDialogComponent {
  exclusiveContent?: IExclusiveContent;

  constructor(
    protected exclusiveContentService: ExclusiveContentService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.exclusiveContentService.delete(id).subscribe(() => {
      this.eventManager.broadcast('exclusiveContentListModification');
      this.activeModal.close();
    });
  }
}
