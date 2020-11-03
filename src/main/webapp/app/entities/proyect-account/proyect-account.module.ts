import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Fun4FundSharedModule } from 'app/shared/shared.module';
import { ProyectAccountComponent } from './proyect-account.component';
import { ProyectAccountDetailComponent } from './proyect-account-detail.component';
import { ProyectAccountUpdateComponent } from './proyect-account-update.component';
import { ProyectAccountDeleteDialogComponent } from './proyect-account-delete-dialog.component';
import { proyectAccountRoute } from './proyect-account.route';

@NgModule({
  imports: [Fun4FundSharedModule, RouterModule.forChild(proyectAccountRoute)],
  declarations: [
    ProyectAccountComponent,
    ProyectAccountDetailComponent,
    ProyectAccountUpdateComponent,
    ProyectAccountDeleteDialogComponent,
  ],
  entryComponents: [ProyectAccountDeleteDialogComponent],
})
export class Fun4FundProyectAccountModule {}
