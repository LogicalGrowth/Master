import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAdminPreferences } from 'app/shared/model/admin-preferences.model';
import { AdminPreferencesService } from './admin-preferences.service';
import { AdminPreferencesDeleteDialogComponent } from './admin-preferences-delete-dialog.component';

@Component({
  selector: 'jhi-admin-preferences',
  templateUrl: './admin-preferences.component.html',
})
export class AdminPreferencesComponent implements OnInit, OnDestroy {
  adminPreferences?: IAdminPreferences[];
  eventSubscriber?: Subscription;

  constructor(
    protected adminPreferencesService: AdminPreferencesService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.adminPreferencesService.query().subscribe((res: HttpResponse<IAdminPreferences[]>) => (this.adminPreferences = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInAdminPreferences();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IAdminPreferences): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInAdminPreferences(): void {
    this.eventSubscriber = this.eventManager.subscribe('adminPreferencesListModification', () => this.loadAll());
  }

  delete(adminPreferences: IAdminPreferences): void {
    const modalRef = this.modalService.open(AdminPreferencesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.adminPreferences = adminPreferences;
  }
}
