import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Fun4FundSharedModule } from 'app/shared/shared.module';
import { DonationHistoryComponent } from './donation-history.component';
import { DonationHistoryDetailComponent } from './donation-history-detail.component';
import { DonationHistoryUpdateComponent } from './donation-history-update.component';
import { DonationHistoryDeleteDialogComponent } from './donation-history-delete-dialog.component';
import { donationHistoryRoute } from './donation-history.route';

@NgModule({
  imports: [Fun4FundSharedModule, RouterModule.forChild(donationHistoryRoute)],
  declarations: [
    DonationHistoryComponent,
    DonationHistoryDetailComponent,
    DonationHistoryUpdateComponent,
    DonationHistoryDeleteDialogComponent,
  ],
  entryComponents: [DonationHistoryDeleteDialogComponent],
})
export class Fun4FundDonationHistoryModule {}
