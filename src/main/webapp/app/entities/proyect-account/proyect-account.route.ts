import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IProyectAccount, ProyectAccount } from 'app/shared/model/proyect-account.model';
import { ProyectAccountService } from './proyect-account.service';
import { ProyectAccountComponent } from './proyect-account.component';
import { ProyectAccountDetailComponent } from './proyect-account-detail.component';
import { ProyectAccountUpdateComponent } from './proyect-account-update.component';

@Injectable({ providedIn: 'root' })
export class ProyectAccountResolve implements Resolve<IProyectAccount> {
  constructor(private service: ProyectAccountService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProyectAccount> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((proyectAccount: HttpResponse<ProyectAccount>) => {
          if (proyectAccount.body) {
            return of(proyectAccount.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ProyectAccount());
  }
}

export const proyectAccountRoute: Routes = [
  {
    path: '',
    component: ProyectAccountComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.proyectAccount.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProyectAccountDetailComponent,
    resolve: {
      proyectAccount: ProyectAccountResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.proyectAccount.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProyectAccountUpdateComponent,
    resolve: {
      proyectAccount: ProyectAccountResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.proyectAccount.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProyectAccountUpdateComponent,
    resolve: {
      proyectAccount: ProyectAccountResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.proyectAccount.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
