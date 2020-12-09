import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Fun4FundSharedModule } from 'app/shared/shared.module';
import { FavoriteComponent } from './favorite.component';
import { FavoriteDetailComponent } from './favorite-detail.component';
import { FavoriteUpdateComponent } from './favorite-update.component';
import { FavoriteDeleteDialogComponent } from './favorite-delete-dialog.component';
import { favoriteRoute } from './favorite.route';

@NgModule({
  imports: [Fun4FundSharedModule, RouterModule.forChild(favoriteRoute)],
  declarations: [FavoriteComponent, FavoriteDetailComponent, FavoriteUpdateComponent, FavoriteDeleteDialogComponent],
  entryComponents: [FavoriteDeleteDialogComponent],
})
export class Fun4FundFavoriteModule {}
