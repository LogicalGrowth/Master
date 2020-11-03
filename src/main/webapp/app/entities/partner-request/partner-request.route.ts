import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPartnerRequest, PartnerRequest } from 'app/shared/model/partner-request.model';
import { PartnerRequestService } from './partner-request.service';
import { PartnerRequestComponent } from './partner-request.component';
import { PartnerRequestDetailComponent } from './partner-request-detail.component';
import { PartnerRequestUpdateComponent } from './partner-request-update.component';

@Injectable({ providedIn: 'root' })
export class PartnerRequestResolve implements Resolve<IPartnerRequest> {
  constructor(private service: PartnerRequestService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPartnerRequest> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((partnerRequest: HttpResponse<PartnerRequest>) => {
          if (partnerRequest.body) {
            return of(partnerRequest.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PartnerRequest());
  }
}

export const partnerRequestRoute: Routes = [
  {
    path: '',
    component: PartnerRequestComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.partnerRequest.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PartnerRequestDetailComponent,
    resolve: {
      partnerRequest: PartnerRequestResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.partnerRequest.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PartnerRequestUpdateComponent,
    resolve: {
      partnerRequest: PartnerRequestResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.partnerRequest.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PartnerRequestUpdateComponent,
    resolve: {
      partnerRequest: PartnerRequestResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.partnerRequest.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
