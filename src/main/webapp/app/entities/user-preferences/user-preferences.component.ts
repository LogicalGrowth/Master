import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserPreferences } from 'app/shared/model/user-preferences.model';
import { UserPreferencesService } from './user-preferences.service';
import { UserPreferencesDeleteDialogComponent } from './user-preferences-delete-dialog.component';

@Component({
  selector: 'jhi-user-preferences',
  templateUrl: './user-preferences.component.html',
})
export class UserPreferencesComponent implements OnInit, OnDestroy {
  userPreferences?: IUserPreferences[];
  eventSubscriber?: Subscription;

  constructor(
    protected userPreferencesService: UserPreferencesService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.userPreferencesService.query().subscribe((res: HttpResponse<IUserPreferences[]>) => (this.userPreferences = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInUserPreferences();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IUserPreferences): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInUserPreferences(): void {
    this.eventSubscriber = this.eventManager.subscribe('userPreferencesListModification', () => this.loadAll());
  }

  delete(userPreferences: IUserPreferences): void {
    const modalRef = this.modalService.open(UserPreferencesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.userPreferences = userPreferences;
  }
}
