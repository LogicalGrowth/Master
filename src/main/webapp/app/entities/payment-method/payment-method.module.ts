import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Fun4FundSharedModule } from 'app/shared/shared.module';
import { PaymentMethodComponent } from './payment-method.component';
import { PaymentMethodDetailComponent } from './payment-method-detail.component';
import { PaymentMethodUpdateComponent } from './payment-method-update.component';
import { PaymentMethodDeleteDialogComponent } from './payment-method-delete-dialog.component';
import { paymentMethodRoute } from './payment-method.route';
import { ComponentsModule } from '../../components/components.module';
import { PaymentMethodSelectDialogComponent } from './payment-method-select-dialog';

@NgModule({
  imports: [Fun4FundSharedModule, RouterModule.forChild(paymentMethodRoute), ComponentsModule],
  declarations: [
    PaymentMethodComponent,
    PaymentMethodDetailComponent,
    PaymentMethodUpdateComponent,
    PaymentMethodDeleteDialogComponent,
    PaymentMethodSelectDialogComponent,
  ],
  entryComponents: [PaymentMethodDeleteDialogComponent, PaymentMethodSelectDialogComponent],
})
export class Fun4FundPaymentMethodModule {}
