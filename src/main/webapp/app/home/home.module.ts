import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Fun4FundSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { ComponentsModule } from 'app/components/components.module';

@NgModule({
  imports: [Fun4FundSharedModule, RouterModule.forChild([HOME_ROUTE]), ComponentsModule],
  declarations: [HomeComponent],
})
export class Fun4FundHomeModule {}
