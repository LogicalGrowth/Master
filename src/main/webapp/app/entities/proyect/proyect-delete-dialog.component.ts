import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProyect } from 'app/shared/model/proyect.model';
import { ProyectService } from './proyect.service';

@Component({
  templateUrl: './proyect-delete-dialog.component.html',
})
export class ProyectDeleteDialogComponent {
  proyect?: IProyect;

  constructor(protected proyectService: ProyectService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.proyectService.delete(id).subscribe(() => {
      this.eventManager.broadcast('proyectListModification');
      this.activeModal.close();
    });
  }
}
