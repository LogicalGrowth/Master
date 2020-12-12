import { HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PartnerRequestService } from 'app/entities/partner-request/partner-request.service';
import { IPartnerRequest } from 'app/shared/model/partner-request.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PartnerRequestDeleteDialogComponent } from 'app/entities/partner-request/partner-request-delete-dialog.component';
import { RequestStatus } from 'app/shared/model/enumerations/request-status.model';
import { PartnerRequestAcceptDialogComponent } from 'app/entities/partner-request/partner-request-accept-dialog.component';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

@Component({
  selector: 'jhi-partner-request-list',
  templateUrl: './partner-request-list.component.html',
  styleUrls: ['../../../../content/scss/paper-dashboard.scss'],
})
export class PartnerRequestListComponent implements OnInit, OnDestroy {
  partnerRequests?: IPartnerRequest[];
  eventSubscriber?: Subscription;
  id: any;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected partnerRequestService: PartnerRequestService,
    protected modalService: NgbModal,
    protected eventManager: JhiEventManager,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPartnerRequest();
  }

  loadAll(): void {
    this.activatedRoute.data.subscribe(({ proyect }) => {
      this.id = proyect.id;
      this.partnerRequestService
        .query({ 'proyectId.equals': proyect.id, 'status.equals': RequestStatus.SEND })
        .subscribe((data: HttpResponse<IPartnerRequest[]>) => {
          this.partnerRequests = data.body || [];
        });
    });
  }

  delete(partnerRequest: IPartnerRequest): void {
    const modalRef = this.modalService.open(PartnerRequestDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.partnerRequest = partnerRequest;
  }

  accept(partnerRequest: IPartnerRequest): void {
    const modalRef = this.modalService.open(PartnerRequestAcceptDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.partnerRequest = partnerRequest;
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  registerChangeInPartnerRequest(): void {
    this.eventSubscriber = this.eventManager.subscribe('partnerRequestListModification', () => this.loadAll());
  }

  goToProyect(): void {
    this.router.navigate(['/proyect/' + this.id + '/view']);
  }
}
