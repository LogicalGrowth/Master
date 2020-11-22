import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Fun4FundSharedModule } from 'app/shared/shared.module';
import { AuctionComponent } from './auction.component';
import { AuctionDetailComponent } from './auction-detail.component';
import { AuctionUpdateComponent } from './auction-update.component';
import { AuctionDeleteDialogComponent } from './auction-delete-dialog.component';
import { auctionRoute } from './auction.route';
import { ComponentsModule } from '../../components/components.module';

@NgModule({
  imports: [Fun4FundSharedModule, RouterModule.forChild(auctionRoute), ComponentsModule],
  declarations: [AuctionComponent, AuctionDetailComponent, AuctionUpdateComponent, AuctionDeleteDialogComponent],
  entryComponents: [AuctionDeleteDialogComponent],
})
export class Fun4FundAuctionModule {}
