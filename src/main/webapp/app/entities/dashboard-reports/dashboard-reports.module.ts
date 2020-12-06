import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ComponentsModule } from 'app/components/components.module';
import { Fun4FundSharedModule } from 'app/shared/shared.module';
import { DashboardReportsComponent } from './dashboard-reports.component';
import { dashboardReportsRoute } from './dashboard-reports.route';

@NgModule({
  imports: [Fun4FundSharedModule, RouterModule.forChild(dashboardReportsRoute), ComponentsModule],
  declarations: [DashboardReportsComponent],
  entryComponents: [],
})
export class Fun4FundDashboardReportsModule {}
