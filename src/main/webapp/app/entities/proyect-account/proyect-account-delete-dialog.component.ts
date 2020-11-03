import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProyectAccount } from 'app/shared/model/proyect-account.model';
import { ProyectAccountService } from './proyect-account.service';

@Component({
  templateUrl: './proyect-account-delete-dialog.component.html',
})
export class ProyectAccountDeleteDialogComponent {
  proyectAccount?: IProyectAccount;

  constructor(
    protected proyectAccountService: ProyectAccountService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.proyectAccountService.delete(id).subscribe(() => {
      this.eventManager.broadcast('proyectAccountListModification');
      this.activeModal.close();
    });
  }
}
