import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Fun4FundSharedModule } from 'app/shared/shared.module';
import { CheckpointComponent } from './checkpoint.component';
import { CheckpointDetailComponent } from './checkpoint-detail.component';
import { CheckpointUpdateComponent } from './checkpoint-update.component';
import { CheckpointDeleteDialogComponent } from './checkpoint-delete-dialog.component';
import { checkpointRoute } from './checkpoint.route';

@NgModule({
  imports: [Fun4FundSharedModule, RouterModule.forChild(checkpointRoute)],
  declarations: [CheckpointComponent, CheckpointDetailComponent, CheckpointUpdateComponent, CheckpointDeleteDialogComponent],
  entryComponents: [CheckpointDeleteDialogComponent],
})
export class Fun4FundCheckpointModule {}
