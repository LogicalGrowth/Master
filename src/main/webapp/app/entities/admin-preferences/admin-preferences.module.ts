import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Fun4FundSharedModule } from 'app/shared/shared.module';
import { AdminPreferencesComponent } from './admin-preferences.component';
import { AdminPreferencesDetailComponent } from './admin-preferences-detail.component';
import { AdminPreferencesUpdateComponent } from './admin-preferences-update.component';
import { AdminPreferencesDeleteDialogComponent } from './admin-preferences-delete-dialog.component';
import { adminPreferencesRoute } from './admin-preferences.route';

@NgModule({
  imports: [Fun4FundSharedModule, RouterModule.forChild(adminPreferencesRoute)],
  declarations: [
    AdminPreferencesComponent,
    AdminPreferencesDetailComponent,
    AdminPreferencesUpdateComponent,
    AdminPreferencesDeleteDialogComponent,
  ],
  entryComponents: [AdminPreferencesDeleteDialogComponent],
})
export class Fun4FundAdminPreferencesModule {}
