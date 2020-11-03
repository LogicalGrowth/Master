import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPasswordHistory, PasswordHistory } from 'app/shared/model/password-history.model';
import { PasswordHistoryService } from './password-history.service';
import { PasswordHistoryComponent } from './password-history.component';
import { PasswordHistoryDetailComponent } from './password-history-detail.component';
import { PasswordHistoryUpdateComponent } from './password-history-update.component';

@Injectable({ providedIn: 'root' })
export class PasswordHistoryResolve implements Resolve<IPasswordHistory> {
  constructor(private service: PasswordHistoryService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPasswordHistory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((passwordHistory: HttpResponse<PasswordHistory>) => {
          if (passwordHistory.body) {
            return of(passwordHistory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PasswordHistory());
  }
}

export const passwordHistoryRoute: Routes = [
  {
    path: '',
    component: PasswordHistoryComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.passwordHistory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PasswordHistoryDetailComponent,
    resolve: {
      passwordHistory: PasswordHistoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.passwordHistory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PasswordHistoryUpdateComponent,
    resolve: {
      passwordHistory: PasswordHistoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.passwordHistory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PasswordHistoryUpdateComponent,
    resolve: {
      passwordHistory: PasswordHistoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.passwordHistory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
