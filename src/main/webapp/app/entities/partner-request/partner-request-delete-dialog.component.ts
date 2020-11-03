import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPartnerRequest } from 'app/shared/model/partner-request.model';
import { PartnerRequestService } from './partner-request.service';

@Component({
  templateUrl: './partner-request-delete-dialog.component.html',
})
export class PartnerRequestDeleteDialogComponent {
  partnerRequest?: IPartnerRequest;

  constructor(
    protected partnerRequestService: PartnerRequestService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.partnerRequestService.delete(id).subscribe(() => {
      this.eventManager.broadcast('partnerRequestListModification');
      this.activeModal.close();
    });
  }
}
