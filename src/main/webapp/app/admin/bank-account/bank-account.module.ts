import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Fun4FundSharedModule } from 'app/shared/shared.module';

import { BankAccountComponent } from './bank-account.component';

import { BankAccountRoute } from './bank-account.route';

@NgModule({
  imports: [Fun4FundSharedModule, RouterModule.forChild([BankAccountRoute])],
  declarations: [BankAccountComponent],
})
export class BankAccountModule {}
