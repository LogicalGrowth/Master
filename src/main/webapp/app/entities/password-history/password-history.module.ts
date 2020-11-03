import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Fun4FundSharedModule } from 'app/shared/shared.module';
import { PasswordHistoryComponent } from './password-history.component';
import { PasswordHistoryDetailComponent } from './password-history-detail.component';
import { PasswordHistoryUpdateComponent } from './password-history-update.component';
import { PasswordHistoryDeleteDialogComponent } from './password-history-delete-dialog.component';
import { passwordHistoryRoute } from './password-history.route';

@NgModule({
  imports: [Fun4FundSharedModule, RouterModule.forChild(passwordHistoryRoute)],
  declarations: [
    PasswordHistoryComponent,
    PasswordHistoryDetailComponent,
    PasswordHistoryUpdateComponent,
    PasswordHistoryDeleteDialogComponent,
  ],
  entryComponents: [PasswordHistoryDeleteDialogComponent],
})
export class Fun4FundPasswordHistoryModule {}
