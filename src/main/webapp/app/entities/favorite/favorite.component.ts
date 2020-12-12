import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFavorite } from 'app/shared/model/favorite.model';
import { FavoriteService } from './favorite.service';
import { FavoriteDeleteDialogComponent } from './favorite-delete-dialog.component';

@Component({
  selector: 'jhi-favorite',
  templateUrl: './favorite.component.html',
})
export class FavoriteComponent implements OnInit, OnDestroy {
  favorites?: IFavorite[];
  eventSubscriber?: Subscription;

  constructor(protected favoriteService: FavoriteService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.favoriteService.query().subscribe((res: HttpResponse<IFavorite[]>) => (this.favorites = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFavorites();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IFavorite): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInFavorites(): void {
    this.eventSubscriber = this.eventManager.subscribe('favoriteListModification', () => this.loadAll());
  }

  delete(favorite: IFavorite): void {
    const modalRef = this.modalService.open(FavoriteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.favorite = favorite;
  }
}
