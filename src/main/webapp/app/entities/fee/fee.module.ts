import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Fun4FundSharedModule } from 'app/shared/shared.module';
import { FeeComponent } from './fee.component';
import { FeeDetailComponent } from './fee-detail.component';
import { FeeUpdateComponent } from './fee-update.component';
import { FeeDeleteDialogComponent } from './fee-delete-dialog.component';
import { feeRoute } from './fee.route';

@NgModule({
  imports: [Fun4FundSharedModule, RouterModule.forChild(feeRoute)],
  declarations: [FeeComponent, FeeDetailComponent, FeeUpdateComponent, FeeDeleteDialogComponent],
  entryComponents: [FeeDeleteDialogComponent],
})
export class Fun4FundFeeModule {}
