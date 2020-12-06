import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Fun4FundSharedModule } from 'app/shared/shared.module';
import { PartnerRequestComponent } from './partner-request.component';
import { PartnerRequestDetailComponent } from './partner-request-detail.component';
import { PartnerRequestUpdateComponent } from './partner-request-update.component';
import { PartnerRequestDeleteDialogComponent } from './partner-request-delete-dialog.component';
import { PartnerRequestAcceptDialogComponent } from './partner-request-accept-dialog.component';
import { partnerRequestRoute } from './partner-request.route';

@NgModule({
  imports: [Fun4FundSharedModule, RouterModule.forChild(partnerRequestRoute)],
  declarations: [
    PartnerRequestComponent,
    PartnerRequestDetailComponent,
    PartnerRequestUpdateComponent,
    PartnerRequestDeleteDialogComponent,
    PartnerRequestAcceptDialogComponent,
  ],
  entryComponents: [PartnerRequestDeleteDialogComponent, PartnerRequestAcceptDialogComponent],
})
export class Fun4FundPartnerRequestModule {}
