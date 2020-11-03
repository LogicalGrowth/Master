import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFee } from 'app/shared/model/fee.model';
import { FeeService } from './fee.service';
import { FeeDeleteDialogComponent } from './fee-delete-dialog.component';

@Component({
  selector: 'jhi-fee',
  templateUrl: './fee.component.html',
})
export class FeeComponent implements OnInit, OnDestroy {
  fees?: IFee[];
  eventSubscriber?: Subscription;

  constructor(protected feeService: FeeService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.feeService.query().subscribe((res: HttpResponse<IFee[]>) => (this.fees = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFees();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IFee): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInFees(): void {
    this.eventSubscriber = this.eventManager.subscribe('feeListModification', () => this.loadAll());
  }

  delete(fee: IFee): void {
    const modalRef = this.modalService.open(FeeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.fee = fee;
  }
}
