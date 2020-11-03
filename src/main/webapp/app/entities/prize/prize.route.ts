import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPrize, Prize } from 'app/shared/model/prize.model';
import { PrizeService } from './prize.service';
import { PrizeComponent } from './prize.component';
import { PrizeDetailComponent } from './prize-detail.component';
import { PrizeUpdateComponent } from './prize-update.component';

@Injectable({ providedIn: 'root' })
export class PrizeResolve implements Resolve<IPrize> {
  constructor(private service: PrizeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrize> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((prize: HttpResponse<Prize>) => {
          if (prize.body) {
            return of(prize.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Prize());
  }
}

export const prizeRoute: Routes = [
  {
    path: '',
    component: PrizeComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.prize.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PrizeDetailComponent,
    resolve: {
      prize: PrizeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.prize.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PrizeUpdateComponent,
    resolve: {
      prize: PrizeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.prize.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PrizeUpdateComponent,
    resolve: {
      prize: PrizeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.prize.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
