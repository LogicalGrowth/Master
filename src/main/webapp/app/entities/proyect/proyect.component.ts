import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IProyect } from 'app/shared/model/proyect.model';
import { ProyectService } from './proyect.service';
import { ProyectDeleteDialogComponent } from './proyect-delete-dialog.component';
import * as moment from 'moment';
import { User } from '../../core/user/user.model';
import { AccountService } from '../../core/auth/account.service';
import { IResource } from '../../shared/model/resource.model';
import { IApplicationUser } from '../../shared/model/application-user.model';
import { ApplicationUserService } from '../application-user/application-user.service';
import { FavoriteService } from '../favorite/favorite.service';
import { Favorite, IFavorite } from 'app/shared/model/favorite.model';

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
  favorites?: IFavorite[];

  constructor(
    protected proyectService: ProyectService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    private accountService: AccountService,
    private applicationUserService: ApplicationUserService,
    private favoriteService: FavoriteService
  ) {}

  loadAll(): void {
    this.proyectService.query({ 'status.equals': true }).subscribe((res: HttpResponse<IProyect[]>) => {
      this.proyects = res.body || [];

      this.accountService.identity().subscribe(account => {
        if (account) {
          this.account = account;
        }

        this.applicationUserService
          .query({ 'internalUserId.equals': this.account.id })
          .subscribe((response: HttpResponse<IApplicationUser[]>) => {
            this.applicationUser = response.body || [];

            this.loadFavorites(response.body![0].id as number);
          });
      });
    });
  }

  loadFavorites(id: number): void {
    this.proyects?.forEach(element => {
      this.favoriteService
        .query({ 'proyectId.equals': element.id as number, 'userId.equals': id })
        .subscribe((res: HttpResponse<IFavorite[]>) => {
          element.favorites = res.body || [];

          if (res.body!.length > 0) element.favorite = true;
        });
    });
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInProyects();
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

  addFavorite(proyect: IProyect): void {
    const favorite = {
      ...new Favorite(),
      id: undefined,
      user: this.applicationUser![0],
      proyect,
    };
    this.subscribeToSaveResponse(this.favoriteService.create(favorite));
  }

  removeFavorite(proyect: IProyect): void {
    this.favoriteService
      .query({ 'userId.equals': this.applicationUser![0].id, 'proyectId.equals': proyect.id })
      .subscribe((res: HttpResponse<IFavorite[]>) => {
        this.favorites = res.body || [];
        if (res.body !== []) {
          this.favoriteService.delete(res.body![0].id as number).subscribe(() => {
            this.loadAll();
          });
        }
      });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFavorite>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.loadAll();
  }

  protected onSaveError(): void {}
}
