import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Fun4FundSharedModule } from 'app/shared/shared.module';
import { PrizeComponent } from './prize.component';
import { PrizeDetailComponent } from './prize-detail.component';
import { PrizeUpdateComponent } from './prize-update.component';
import { PrizeDeleteDialogComponent } from './prize-delete-dialog.component';
import { prizeRoute } from './prize.route';

@NgModule({
  imports: [Fun4FundSharedModule, RouterModule.forChild(prizeRoute)],
  declarations: [PrizeComponent, PrizeDetailComponent, PrizeUpdateComponent, PrizeDeleteDialogComponent],
  entryComponents: [PrizeDeleteDialogComponent],
})
export class Fun4FundPrizeModule {}
