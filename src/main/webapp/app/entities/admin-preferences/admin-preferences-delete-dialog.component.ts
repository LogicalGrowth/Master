import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAdminPreferences } from 'app/shared/model/admin-preferences.model';
import { AdminPreferencesService } from './admin-preferences.service';

@Component({
  templateUrl: './admin-preferences-delete-dialog.component.html',
})
export class AdminPreferencesDeleteDialogComponent {
  adminPreferences?: IAdminPreferences;

  constructor(
    protected adminPreferencesService: AdminPreferencesService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.adminPreferencesService.delete(id).subscribe(() => {
      this.eventManager.broadcast('adminPreferencesListModification');
      this.activeModal.close();
    });
  }
}
