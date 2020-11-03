import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Fun4FundSharedModule } from 'app/shared/shared.module';
import { ApplicationUserComponent } from './application-user.component';
import { ApplicationUserDetailComponent } from './application-user-detail.component';
import { ApplicationUserUpdateComponent } from './application-user-update.component';
import { ApplicationUserDeleteDialogComponent } from './application-user-delete-dialog.component';
import { applicationUserRoute } from './application-user.route';

@NgModule({
  imports: [Fun4FundSharedModule, RouterModule.forChild(applicationUserRoute)],
  declarations: [
    ApplicationUserComponent,
    ApplicationUserDetailComponent,
    ApplicationUserUpdateComponent,
    ApplicationUserDeleteDialogComponent,
  ],
  entryComponents: [ApplicationUserDeleteDialogComponent],
})
export class Fun4FundApplicationUserModule {}
