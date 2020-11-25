import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IConfigSystem } from 'app/shared/model/config-system.model';
import { ConfigSystemService } from './config-system.service';

@Component({
  templateUrl: './config-system-delete-dialog.component.html',
})
export class ConfigSystemDeleteDialogComponent {
  configSystem?: IConfigSystem;

  constructor(
    protected configSystemService: ConfigSystemService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.configSystemService.delete(id).subscribe(() => {
      this.eventManager.broadcast('configSystemListModification');
      this.activeModal.close();
    });
  }
}
