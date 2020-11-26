import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IProyect } from 'app/shared/model/proyect.model';
import { PartnerRequestComponent } from './partner-request.component';

@Injectable({
  providedIn: 'root',
})
export class PartnerRequestModalService {
  private isOpen = false;

  constructor(private modalService: NgbModal) {}

  open(proyect: IProyect): void {
    if (this.isOpen) {
      return;
    }
    this.isOpen = true;
    const modalRef: NgbModalRef = this.modalService.open(PartnerRequestComponent);
    modalRef.componentInstance.proyect = proyect;
    modalRef.result.finally(() => (this.isOpen = false));
  }

  close(): void {
    this.isOpen = false;
    this.modalService.dismissAll();
  }
}
