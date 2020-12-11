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
import { CategoryService } from '../category/category.service';
import { ICategory } from '../../shared/model/category.model';
import { CategoryStatus } from '../../shared/model/enumerations/category-status.model';

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
  categories?: ICategory[];
  description: string;
  profitable: boolean;
  nonprofit: boolean;
  category: number;
  sortBy: string;

  constructor(
    protected proyectService: ProyectService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    private accountService: AccountService,
    private resourceService: ResourceService,
    private applicationUserService: ApplicationUserService,
    protected categoryService: CategoryService
  ) {
    this.description = '';
    this.profitable = true;
    this.nonprofit = true;
    this.category = -1;
    this.sortBy = 'creationDate';
  }

  loadAll(): void {
    this.proyectService.query().subscribe((res: HttpResponse<IProyect[]>) => (this.proyects = res.body || []));
  }

  loadFilter(): void {
    const showAllCategories = this.category === -1;

    const queryName = { 'Name.contains': this.description };
    const queryCategory = showAllCategories ? {} : { 'Name.contains': this.description, 'categoryId.equals': this.category };
    const queryProjectType = this.profitable && this.nonprofit ? {} : { 'idType.equals': this.profitable ? 'PROFITABLE' : 'NONPROFIT' };

    const finalQuery = Object.assign(queryName, queryCategory, queryProjectType);

    this.proyectService.query(finalQuery).subscribe((res: HttpResponse<IProyect[]>) => (this.proyects = res.body || []));
  }

  clearFilters(): void {
    if (this.description !== '' || !this.nonprofit || !this.profitable || this.category !== -1) {
      this.description = '';
      this.profitable = true;
      this.nonprofit = true;
      this.category = -1;
      this.sortBy = 'creationDate';

      this.loadAll();
    }
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInProyects();

    this.categoryService
      .query({ 'State.equals': CategoryStatus.ENABLED })
      .subscribe((res: HttpResponse<ICategory[]>) => (this.categories = res.body || []));

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

  topFunction(): void {
    document.body.scrollTop = 0;
    document.documentElement.scrollTop = 0;
  }
}
