import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAppLog, AppLog } from 'app/shared/model/app-log.model';
import { AppLogService } from './app-log.service';
import { AppLogComponent } from './app-log.component';
import { AppLogDetailComponent } from './app-log-detail.component';
import { AppLogUpdateComponent } from './app-log-update.component';

@Injectable({ providedIn: 'root' })
export class AppLogResolve implements Resolve<IAppLog> {
  constructor(private service: AppLogService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAppLog> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((appLog: HttpResponse<AppLog>) => {
          if (appLog.body) {
            return of(appLog.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AppLog());
  }
}

export const appLogRoute: Routes = [
  {
    path: '',
    component: AppLogComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.appLog.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AppLogDetailComponent,
    resolve: {
      appLog: AppLogResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.appLog.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AppLogUpdateComponent,
    resolve: {
      appLog: AppLogResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.appLog.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AppLogUpdateComponent,
    resolve: {
      appLog: AppLogResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.appLog.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
