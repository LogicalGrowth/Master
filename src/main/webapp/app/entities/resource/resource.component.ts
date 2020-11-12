import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IResource } from 'app/shared/model/resource.model';
import { ResourceService } from './resource.service';
import { ResourceDeleteDialogComponent } from './resource-delete-dialog.component';

@Component({
  selector: 'jhi-resource',
  templateUrl: './resource.component.html',
})
export class ResourceComponent implements OnInit, OnDestroy {
  resources?: IResource[];
  eventSubscriber?: Subscription;

  constructor(protected resourceService: ResourceService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.resourceService.query().subscribe((res: HttpResponse<IResource[]>) => (this.resources = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInResources();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IResource): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInResources(): void {
    this.eventSubscriber = this.eventManager.subscribe('resourceListModification', () => this.loadAll());
  }

  delete(resource: IResource): void {
    const modalRef = this.modalService.open(ResourceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.resource = resource;
  }
}
