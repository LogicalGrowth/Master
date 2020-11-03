import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPartnerRequest } from 'app/shared/model/partner-request.model';
import { PartnerRequestService } from './partner-request.service';
import { PartnerRequestDeleteDialogComponent } from './partner-request-delete-dialog.component';

@Component({
  selector: 'jhi-partner-request',
  templateUrl: './partner-request.component.html',
})
export class PartnerRequestComponent implements OnInit, OnDestroy {
  partnerRequests?: IPartnerRequest[];
  eventSubscriber?: Subscription;

  constructor(
    protected partnerRequestService: PartnerRequestService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.partnerRequestService.query().subscribe((res: HttpResponse<IPartnerRequest[]>) => (this.partnerRequests = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPartnerRequests();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPartnerRequest): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPartnerRequests(): void {
    this.eventSubscriber = this.eventManager.subscribe('partnerRequestListModification', () => this.loadAll());
  }

  delete(partnerRequest: IPartnerRequest): void {
    const modalRef = this.modalService.open(PartnerRequestDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.partnerRequest = partnerRequest;
  }
}
