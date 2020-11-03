import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IUserPreferences, UserPreferences } from 'app/shared/model/user-preferences.model';
import { UserPreferencesService } from './user-preferences.service';
import { UserPreferencesComponent } from './user-preferences.component';
import { UserPreferencesDetailComponent } from './user-preferences-detail.component';
import { UserPreferencesUpdateComponent } from './user-preferences-update.component';

@Injectable({ providedIn: 'root' })
export class UserPreferencesResolve implements Resolve<IUserPreferences> {
  constructor(private service: UserPreferencesService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUserPreferences> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((userPreferences: HttpResponse<UserPreferences>) => {
          if (userPreferences.body) {
            return of(userPreferences.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UserPreferences());
  }
}

export const userPreferencesRoute: Routes = [
  {
    path: '',
    component: UserPreferencesComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.userPreferences.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserPreferencesDetailComponent,
    resolve: {
      userPreferences: UserPreferencesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.userPreferences.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserPreferencesUpdateComponent,
    resolve: {
      userPreferences: UserPreferencesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.userPreferences.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserPreferencesUpdateComponent,
    resolve: {
      userPreferences: UserPreferencesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.userPreferences.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
