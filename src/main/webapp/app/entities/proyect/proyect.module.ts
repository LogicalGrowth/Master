import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Fun4FundSharedModule } from 'app/shared/shared.module';
import { ProyectComponent } from './proyect.component';
import { ProyectDetailComponent } from './proyect-detail.component';
import { ProyectUpdateComponent } from './proyect-update.component';
import { ProyectDeleteDialogComponent } from './proyect-delete-dialog.component';
import { proyectRoute } from './proyect.route';
import { ComponentsModule } from 'app/components/components.module';

@NgModule({
  imports: [Fun4FundSharedModule, ComponentsModule, RouterModule.forChild(proyectRoute)],
  declarations: [ProyectComponent, ProyectDetailComponent, ProyectUpdateComponent, ProyectDeleteDialogComponent],
  entryComponents: [ProyectDeleteDialogComponent],
})
export class Fun4FundProyectModule {}
