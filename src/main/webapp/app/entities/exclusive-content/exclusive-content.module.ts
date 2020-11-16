import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Fun4FundSharedModule } from 'app/shared/shared.module';
import { ExclusiveContentComponent } from './exclusive-content.component';
import { ExclusiveContentDetailComponent } from './exclusive-content-detail.component';
import { ExclusiveContentUpdateComponent } from './exclusive-content-update.component';
import { ExclusiveContentDeleteDialogComponent } from './exclusive-content-delete-dialog.component';
import { exclusiveContentRoute } from './exclusive-content.route';
import { ComponentsModule } from 'app/components/components.module';

@NgModule({
  imports: [Fun4FundSharedModule, ComponentsModule, RouterModule.forChild(exclusiveContentRoute)],
  declarations: [
    ExclusiveContentComponent,
    ExclusiveContentDetailComponent,
    ExclusiveContentUpdateComponent,
    ExclusiveContentDeleteDialogComponent,
  ],
  entryComponents: [ExclusiveContentDeleteDialogComponent],
})
export class Fun4FundExclusiveContentModule {}
