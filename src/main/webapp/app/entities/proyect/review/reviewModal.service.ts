import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IProyect } from 'app/shared/model/proyect.model';
import { ReviewComponent } from './review.component';

@Injectable({
  providedIn: 'root',
})
export class ReviewModalService {
  private isOpen = false;

  constructor(private modalService: NgbModal) {}

  open(proyect: IProyect): void {
    if (this.isOpen) {
      return;
    }
    this.isOpen = true;
    const modalRef: NgbModalRef = this.modalService.open(ReviewComponent);
    modalRef.componentInstance.proyect = proyect;
    modalRef.result.finally(() => (this.isOpen = false));
  }

  close(): void {
    this.isOpen = false;
    this.modalService.dismissAll();

    setTimeout(() => {
      location.reload();
    }, 2000);
  }
}
