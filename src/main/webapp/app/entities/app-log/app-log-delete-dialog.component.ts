import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAppLog } from 'app/shared/model/app-log.model';
import { AppLogService } from './app-log.service';

@Component({
  templateUrl: './app-log-delete-dialog.component.html',
})
export class AppLogDeleteDialogComponent {
  appLog?: IAppLog;

  constructor(protected appLogService: AppLogService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.appLogService.delete(id).subscribe(() => {
      this.eventManager.broadcast('appLogListModification');
      this.activeModal.close();
    });
  }
}
