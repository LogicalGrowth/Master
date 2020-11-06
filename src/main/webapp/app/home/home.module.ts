import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Fun4FundSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { TimelineComponent } from '../components/timeline/timeline.component';

@NgModule({
  imports: [Fun4FundSharedModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent, TimelineComponent],
})
export class Fun4FundHomeModule {}
