import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUserPreferences } from 'app/shared/model/user-preferences.model';
import { UserPreferencesService } from './user-preferences.service';

@Component({
  templateUrl: './user-preferences-delete-dialog.component.html',
})
export class UserPreferencesDeleteDialogComponent {
  userPreferences?: IUserPreferences;

  constructor(
    protected userPreferencesService: UserPreferencesService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userPreferencesService.delete(id).subscribe(() => {
      this.eventManager.broadcast('userPreferencesListModification');
      this.activeModal.close();
    });
  }
}
