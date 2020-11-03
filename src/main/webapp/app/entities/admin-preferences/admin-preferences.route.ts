import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAdminPreferences, AdminPreferences } from 'app/shared/model/admin-preferences.model';
import { AdminPreferencesService } from './admin-preferences.service';
import { AdminPreferencesComponent } from './admin-preferences.component';
import { AdminPreferencesDetailComponent } from './admin-preferences-detail.component';
import { AdminPreferencesUpdateComponent } from './admin-preferences-update.component';

@Injectable({ providedIn: 'root' })
export class AdminPreferencesResolve implements Resolve<IAdminPreferences> {
  constructor(private service: AdminPreferencesService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAdminPreferences> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((adminPreferences: HttpResponse<AdminPreferences>) => {
          if (adminPreferences.body) {
            return of(adminPreferences.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AdminPreferences());
  }
}

export const adminPreferencesRoute: Routes = [
  {
    path: '',
    component: AdminPreferencesComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.adminPreferences.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AdminPreferencesDetailComponent,
    resolve: {
      adminPreferences: AdminPreferencesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.adminPreferences.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AdminPreferencesUpdateComponent,
    resolve: {
      adminPreferences: AdminPreferencesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.adminPreferences.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AdminPreferencesUpdateComponent,
    resolve: {
      adminPreferences: AdminPreferencesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.adminPreferences.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
