import { DashboardReportsComponent } from './dashboard-reports.component';
import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Authority } from 'app/shared/constants/authority.constants';

export const dashboardReportsRoute: Routes = [
  {
    path: '',
    component: DashboardReportsComponent,
    data: {
      authorities: [Authority.USER][Authority.ADMIN],
      pageTitle: 'fun4FundApp.dashboardReports.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
