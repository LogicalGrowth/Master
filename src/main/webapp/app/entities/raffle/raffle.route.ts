import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IRaffle, Raffle } from 'app/shared/model/raffle.model';
import { RaffleService } from './raffle.service';
import { RaffleComponent } from './raffle.component';
import { RaffleDetailComponent } from './raffle-detail.component';
import { RaffleUpdateComponent } from './raffle-update.component';
import { ProyectResolve } from '../proyect/proyect.route';

@Injectable({ providedIn: 'root' })
export class RaffleResolve implements Resolve<IRaffle> {
  constructor(private service: RaffleService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRaffle> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((raffle: HttpResponse<Raffle>) => {
          if (raffle.body) {
            return of(raffle.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Raffle());
  }
}

export const raffleRoute: Routes = [
  {
    path: '',
    component: RaffleComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.raffle.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RaffleDetailComponent,
    resolve: {
      raffle: RaffleResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.raffle.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RaffleUpdateComponent,
    resolve: {
      raffle: RaffleResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.raffle.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RaffleUpdateComponent,
    resolve: {
      raffle: RaffleResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.raffle.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/new',
    component: RaffleUpdateComponent,
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
