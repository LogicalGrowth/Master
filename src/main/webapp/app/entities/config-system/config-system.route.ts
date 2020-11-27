import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IConfigSystem, ConfigSystem } from 'app/shared/model/config-system.model';
import { ConfigSystemService } from './config-system.service';
import { ConfigSystemComponent } from './config-system.component';
import { ConfigSystemDetailComponent } from './config-system-detail.component';
import { ConfigSystemUpdateComponent } from './config-system-update.component';

@Injectable({ providedIn: 'root' })
export class ConfigSystemResolve implements Resolve<IConfigSystem> {
  constructor(private service: ConfigSystemService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IConfigSystem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((configSystem: HttpResponse<ConfigSystem>) => {
          if (configSystem.body) {
            return of(configSystem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ConfigSystem());
  }
}

export const configSystemRoute: Routes = [
  {
    path: '',
    component: ConfigSystemComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.configSystem.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConfigSystemDetailComponent,
    resolve: {
      configSystem: ConfigSystemResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.configSystem.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConfigSystemUpdateComponent,
    resolve: {
      configSystem: ConfigSystemResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.configSystem.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConfigSystemUpdateComponent,
    resolve: {
      configSystem: ConfigSystemResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.configSystem.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
