import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Fun4FundSharedModule } from 'app/shared/shared.module';
import { ConfigSystemComponent } from './config-system.component';
import { ConfigSystemDetailComponent } from './config-system-detail.component';
import { ConfigSystemUpdateComponent } from './config-system-update.component';
import { ConfigSystemDeleteDialogComponent } from './config-system-delete-dialog.component';
import { configSystemRoute } from './config-system.route';

@NgModule({
  imports: [Fun4FundSharedModule, RouterModule.forChild(configSystemRoute)],
  declarations: [ConfigSystemComponent, ConfigSystemDetailComponent, ConfigSystemUpdateComponent, ConfigSystemDeleteDialogComponent],
  entryComponents: [ConfigSystemDeleteDialogComponent],
})
export class Fun4FundConfigSystemModule {}
