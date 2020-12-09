import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProyect } from 'app/shared/model/proyect.model';
import { ProyectService } from './proyect.service';
import { ProyectDeleteDialogComponent } from './proyect-delete-dialog.component';
import * as moment from 'moment';
import { User } from '../../core/user/user.model';
import { AccountService } from '../../core/auth/account.service';
import { IResource } from '../../shared/model/resource.model';
import { ResourceService } from '../resource/resource.service';
import { IApplicationUser } from '../../shared/model/application-user.model';
import { ApplicationUserService } from '../application-user/application-user.service';

@Component({
  selector: 'jhi-proyect',
  templateUrl: './proyect.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss', 'project.scss'],
})
export class ProyectComponent implements OnInit, OnDestroy {
  proyects?: IProyect[];
  eventSubscriber?: Subscription;
  updatedDays: any;
  percentile: any;
  isProjectOwner!: Boolean;
  account!: User;
  resource?: IResource[];
  applicationUser?: IApplicationUser[];

  constructor(
    protected proyectService: ProyectService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    private accountService: AccountService,
    private resourceService: ResourceService,
    private applicationUserService: ApplicationUserService
  ) {}

  loadAll(): void {
    this.proyectService.query({ 'status.equals': true }).subscribe((res: HttpResponse<IProyect[]>) => (this.proyects = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInProyects();

    this.accountService.identity().subscribe(account => {
      if (account) {
        this.account = account;
      }

      this.applicationUserService.query({ 'internalUserId.equals': this.account.id }).subscribe((res: HttpResponse<IApplicationUser[]>) => {
        this.applicationUser = res.body || [];
      });
    });
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IProyect): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInProyects(): void {
    this.eventSubscriber = this.eventManager.subscribe('proyectListModification', () => this.loadAll());
  }

  delete(proyect: IProyect): void {
    const modalRef = this.modalService.open(ProyectDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.proyect = proyect;
  }

  getLastUpdate(item: IProyect): any {
    return moment().diff(item.lastUpdated, 'days');
  }

  getPercentile(item: IProyect): any {
    if (item.collected && item.goalAmount) {
      return item.collected > 0 ? Math.floor((100 * item.collected) / item.goalAmount) + '%' : '';
    }
  }

  isProjectAdmin(item: IProyect): boolean {
    return (this.isProjectOwner = this.applicationUser && this.applicationUser[0].id === item.owner?.id ? true : false);
  }
}
