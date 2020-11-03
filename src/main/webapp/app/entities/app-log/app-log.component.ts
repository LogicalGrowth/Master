import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAppLog } from 'app/shared/model/app-log.model';
import { AppLogService } from './app-log.service';
import { AppLogDeleteDialogComponent } from './app-log-delete-dialog.component';

@Component({
  selector: 'jhi-app-log',
  templateUrl: './app-log.component.html',
})
export class AppLogComponent implements OnInit, OnDestroy {
  appLogs?: IAppLog[];
  eventSubscriber?: Subscription;

  constructor(protected appLogService: AppLogService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.appLogService.query().subscribe((res: HttpResponse<IAppLog[]>) => (this.appLogs = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInAppLogs();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IAppLog): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInAppLogs(): void {
    this.eventSubscriber = this.eventManager.subscribe('appLogListModification', () => this.loadAll());
  }

  delete(appLog: IAppLog): void {
    const modalRef = this.modalService.open(AppLogDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.appLog = appLog;
  }
}
