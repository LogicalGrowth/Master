import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IExclusiveContent } from 'app/shared/model/exclusive-content.model';
import { ExclusiveContentService } from './exclusive-content.service';
import { ExclusiveContentDeleteDialogComponent } from './exclusive-content-delete-dialog.component';

@Component({
  selector: 'jhi-exclusive-content',
  templateUrl: './exclusive-content.component.html',
})
export class ExclusiveContentComponent implements OnInit, OnDestroy {
  exclusiveContents?: IExclusiveContent[];
  eventSubscriber?: Subscription;

  constructor(
    protected exclusiveContentService: ExclusiveContentService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.exclusiveContentService.query().subscribe((res: HttpResponse<IExclusiveContent[]>) => (this.exclusiveContents = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInExclusiveContents();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IExclusiveContent): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInExclusiveContents(): void {
    this.eventSubscriber = this.eventManager.subscribe('exclusiveContentListModification', () => this.loadAll());
  }

  delete(exclusiveContent: IExclusiveContent): void {
    const modalRef = this.modalService.open(ExclusiveContentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.exclusiveContent = exclusiveContent;
  }
}
