import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPartnerRequest } from 'app/shared/model/partner-request.model';
import { PartnerRequestService } from './partner-request.service';
import { RequestStatus } from 'app/shared/model/enumerations/request-status.model';

@Component({
  templateUrl: './partner-request-accept-dialog.component.html',
})
export class PartnerRequestAcceptDialogComponent {
  partnerRequest?: IPartnerRequest;

  constructor(
    protected partnerRequestService: PartnerRequestService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmApprove(partnerRequest: IPartnerRequest): void {
    partnerRequest.status = RequestStatus.ACCEPTED;
    this.partnerRequestService.update(partnerRequest).subscribe(() => {
      this.eventManager.broadcast('partnerRequestListModification');
      this.activeModal.close();
    });
  }
}
