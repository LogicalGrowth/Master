import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Fun4FundSharedModule } from 'app/shared/shared.module';
import { RaffleComponent } from './raffle.component';
import { RaffleDetailComponent } from './raffle-detail.component';
import { RaffleUpdateComponent } from './raffle-update.component';
import { RaffleDeleteDialogComponent } from './raffle-delete-dialog.component';
import { raffleRoute } from './raffle.route';

@NgModule({
  imports: [Fun4FundSharedModule, RouterModule.forChild(raffleRoute)],
  declarations: [RaffleComponent, RaffleDetailComponent, RaffleUpdateComponent, RaffleDeleteDialogComponent],
  entryComponents: [RaffleDeleteDialogComponent],
})
export class Fun4FundRaffleModule {}
