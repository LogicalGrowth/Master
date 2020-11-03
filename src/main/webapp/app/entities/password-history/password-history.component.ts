import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPasswordHistory } from 'app/shared/model/password-history.model';
import { PasswordHistoryService } from './password-history.service';
import { PasswordHistoryDeleteDialogComponent } from './password-history-delete-dialog.component';

@Component({
  selector: 'jhi-password-history',
  templateUrl: './password-history.component.html',
})
export class PasswordHistoryComponent implements OnInit, OnDestroy {
  passwordHistories?: IPasswordHistory[];
  eventSubscriber?: Subscription;

  constructor(
    protected passwordHistoryService: PasswordHistoryService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.passwordHistoryService.query().subscribe((res: HttpResponse<IPasswordHistory[]>) => (this.passwordHistories = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPasswordHistories();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPasswordHistory): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPasswordHistories(): void {
    this.eventSubscriber = this.eventManager.subscribe('passwordHistoryListModification', () => this.loadAll());
  }

  delete(passwordHistory: IPasswordHistory): void {
    const modalRef = this.modalService.open(PasswordHistoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.passwordHistory = passwordHistory;
  }
}
