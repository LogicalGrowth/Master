import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Fun4FundSharedModule } from 'app/shared/shared.module';
import { AppLogComponent } from './app-log.component';
import { AppLogDetailComponent } from './app-log-detail.component';
import { AppLogUpdateComponent } from './app-log-update.component';
import { AppLogDeleteDialogComponent } from './app-log-delete-dialog.component';
import { appLogRoute } from './app-log.route';

@NgModule({
  imports: [Fun4FundSharedModule, RouterModule.forChild(appLogRoute)],
  declarations: [AppLogComponent, AppLogDetailComponent, AppLogUpdateComponent, AppLogDeleteDialogComponent],
  entryComponents: [AppLogDeleteDialogComponent],
})
export class Fun4FundAppLogModule {}
