import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICheckpoint } from 'app/shared/model/checkpoint.model';
import { CheckpointService } from './checkpoint.service';
import { CheckpointDeleteDialogComponent } from './checkpoint-delete-dialog.component';

@Component({
  selector: 'jhi-checkpoint',
  templateUrl: './checkpoint.component.html',
})
export class CheckpointComponent implements OnInit, OnDestroy {
  checkpoints?: ICheckpoint[];
  eventSubscriber?: Subscription;

  constructor(protected checkpointService: CheckpointService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.checkpointService.query().subscribe((res: HttpResponse<ICheckpoint[]>) => (this.checkpoints = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCheckpoints();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICheckpoint): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCheckpoints(): void {
    this.eventSubscriber = this.eventManager.subscribe('checkpointListModification', () => this.loadAll());
  }

  delete(checkpoint: ICheckpoint): void {
    const modalRef = this.modalService.open(CheckpointDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.checkpoint = checkpoint;
  }
}
