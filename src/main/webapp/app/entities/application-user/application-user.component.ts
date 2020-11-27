import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IApplicationUser } from 'app/shared/model/application-user.model';
import { ApplicationUserService } from './application-user.service';
import { ApplicationUserDeleteDialogComponent } from './application-user-delete-dialog.component';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-application-user',
  templateUrl: './application-user.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class ApplicationUserComponent implements OnInit, OnDestroy {
  applicationUsers?: IApplicationUser[];
  eventSubscriber?: Subscription;
  user?: IUser[];

  constructor(
    protected applicationUserService: ApplicationUserService,
    protected userService: UserService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.applicationUserService.query().subscribe((res: HttpResponse<IApplicationUser[]>) => (this.applicationUsers = res.body || []));
    this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.user = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInApplicationUsers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  setActive(applicationUser: IApplicationUser, isActivated: boolean): void {
    if (applicationUser.internalUser) {
      applicationUser.internalUser.activated = isActivated;
    }
    this.applicationUserService.update({ ...applicationUser }).subscribe(() => this.loadAll());
  }
  trackId(index: number, item: IApplicationUser): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInApplicationUsers(): void {
    this.eventSubscriber = this.eventManager.subscribe('applicationUserListModification', () => this.loadAll());
  }

  delete(applicationUser: IApplicationUser): void {
    const modalRef = this.modalService.open(ApplicationUserDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.applicationUser = applicationUser;
  }
}
