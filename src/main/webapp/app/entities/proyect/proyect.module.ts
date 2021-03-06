import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NguiMapModule } from '@ngui/map';

import { Fun4FundSharedModule } from 'app/shared/shared.module';
import { ProyectComponent } from './proyect.component';
import { ProyectDetailComponent } from './proyect-detail.component';
import { ProyectUpdateComponent } from './proyect-update.component';
import { ProyectDeleteDialogComponent } from './proyect-delete-dialog.component';
import { proyectRoute } from './proyect.route';
import { ComponentsModule } from 'app/components/components.module';
import { ProyectImageUpdateComponent } from './proyect-image-update/proyect-image-update.component';
import { DonationComponent } from './donation/donation.component';
import { PartnerRequestComponent } from './partner-request/partner-request.component';
import { PartnerRequestListComponent } from './partner-request-list/partner-request-list.component';
import { ProyectManagementComponent } from './proyect-management/proyect-management.component';
import { ExclusiveContentBuyoutComponent } from './exclusive-content-buyout/exclusive-content-buyout.component';
import { TicketComponent } from './ticket/ticket.component';

@NgModule({
  imports: [
    Fun4FundSharedModule,
    RouterModule.forChild(proyectRoute),
    ComponentsModule,
    NguiMapModule.forRoot({ apiUrl: 'https://maps.google.com/maps/api/js?key=AIzaSyAKXA3_YJlVwwt1lx60eO0ljRVUKQO3fNY' }),
  ],
  declarations: [
    ProyectComponent,
    ProyectDetailComponent,
    ProyectUpdateComponent,
    ProyectDeleteDialogComponent,
    ProyectImageUpdateComponent,
    DonationComponent,
    PartnerRequestComponent,
    PartnerRequestListComponent,
    ProyectManagementComponent,
    ExclusiveContentBuyoutComponent,
    TicketComponent,
  ],
  entryComponents: [ProyectDeleteDialogComponent],
})
export class Fun4FundProyectModule {}
