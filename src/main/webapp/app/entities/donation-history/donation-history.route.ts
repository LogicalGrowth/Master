import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDonationHistory, DonationHistory } from 'app/shared/model/donation-history.model';
import { DonationHistoryService } from './donation-history.service';
import { DonationHistoryComponent } from './donation-history.component';
import { DonationHistoryDetailComponent } from './donation-history-detail.component';
import { DonationHistoryUpdateComponent } from './donation-history-update.component';

@Injectable({ providedIn: 'root' })
export class DonationHistoryResolve implements Resolve<IDonationHistory> {
  constructor(private service: DonationHistoryService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDonationHistory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((donationHistory: HttpResponse<DonationHistory>) => {
          if (donationHistory.body) {
            return of(donationHistory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DonationHistory());
  }
}

export const donationHistoryRoute: Routes = [
  {
    path: '',
    component: DonationHistoryComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.donationHistory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DonationHistoryDetailComponent,
    resolve: {
      donationHistory: DonationHistoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.donationHistory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DonationHistoryUpdateComponent,
    resolve: {
      donationHistory: DonationHistoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.donationHistory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DonationHistoryUpdateComponent,
    resolve: {
      donationHistory: DonationHistoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.donationHistory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
