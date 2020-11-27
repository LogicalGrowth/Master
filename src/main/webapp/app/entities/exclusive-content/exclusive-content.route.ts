import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IExclusiveContent, ExclusiveContent } from 'app/shared/model/exclusive-content.model';
import { ExclusiveContentService } from './exclusive-content.service';
import { ExclusiveContentComponent } from './exclusive-content.component';
import { ExclusiveContentDetailComponent } from './exclusive-content-detail.component';
import { ExclusiveContentUpdateComponent } from './exclusive-content-update.component';
import { ProyectResolve } from '../proyect/proyect.route';

@Injectable({ providedIn: 'root' })
export class ExclusiveContentResolve implements Resolve<IExclusiveContent> {
  constructor(private service: ExclusiveContentService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExclusiveContent> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((exclusiveContent: HttpResponse<ExclusiveContent>) => {
          if (exclusiveContent.body) {
            return of(exclusiveContent.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ExclusiveContent());
  }
}

export const exclusiveContentRoute: Routes = [
  {
    path: '',
    component: ExclusiveContentComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.exclusiveContent.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExclusiveContentDetailComponent,
    resolve: {
      exclusiveContent: ExclusiveContentResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.exclusiveContent.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/new',
    component: ExclusiveContentUpdateComponent,
    resolve: {
      currentProyect: ProyectResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.exclusiveContent.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExclusiveContentUpdateComponent,
    resolve: {
      exclusiveContent: ExclusiveContentResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.exclusiveContent.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
