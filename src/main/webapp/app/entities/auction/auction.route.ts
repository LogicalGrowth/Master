import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAuction, Auction } from 'app/shared/model/auction.model';
import { AuctionService } from './auction.service';
import { AuctionComponent } from './auction.component';
import { AuctionDetailComponent } from './auction-detail.component';
import { AuctionUpdateComponent } from './auction-update.component';
import { ExclusiveContentUpdateComponent } from '../exclusive-content/exclusive-content-update.component';
import { ProyectResolve } from '../proyect/proyect.route';

@Injectable({ providedIn: 'root' })
export class AuctionResolve implements Resolve<IAuction> {
  constructor(private service: AuctionService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAuction> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((auction: HttpResponse<Auction>) => {
          if (auction.body) {
            return of(auction.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Auction());
  }
}

export const auctionRoute: Routes = [
  {
    path: '',
    component: AuctionComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.auction.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AuctionDetailComponent,
    resolve: {
      auction: AuctionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.auction.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AuctionUpdateComponent,
    resolve: {
      auction: AuctionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.auction.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AuctionUpdateComponent,
    resolve: {
      auction: AuctionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.auction.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/new',
    component: AuctionUpdateComponent,
    resolve: {
      currentProyect: ProyectResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.exclusiveContent.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
