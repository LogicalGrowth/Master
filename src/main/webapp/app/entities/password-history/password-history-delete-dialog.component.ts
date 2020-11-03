import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPasswordHistory } from 'app/shared/model/password-history.model';
import { PasswordHistoryService } from './password-history.service';

@Component({
  templateUrl: './password-history-delete-dialog.component.html',
})
export class PasswordHistoryDeleteDialogComponent {
  passwordHistory?: IPasswordHistory;

  constructor(
    protected passwordHistoryService: PasswordHistoryService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.passwordHistoryService.delete(id).subscribe(() => {
      this.eventManager.broadcast('passwordHistoryListModification');
      this.activeModal.close();
    });
  }
}
