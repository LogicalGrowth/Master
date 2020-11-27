import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IAuction } from 'app/shared/model/auction.model';
import { BidComponent } from './bid.component';

@Injectable({
  providedIn: 'root',
})
export class BidModalService {
  private isOpen = false;

  constructor(private modalService: NgbModal) {}

  open(auction: IAuction): void {
    if (this.isOpen) {
      return;
    }
    this.isOpen = true;
    const modalRef: NgbModalRef = this.modalService.open(BidComponent);
    modalRef.componentInstance.auction = auction;
    modalRef.result.finally(() => (this.isOpen = false));
  }

  close(): void {
    this.isOpen = false;
    this.modalService.dismissAll();
  }
}
