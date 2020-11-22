import { Route } from '@angular/router';
import { BankAccountComponent } from './bank-account.component';

export const BankAccountRoute: Route = {
  path: '',
  component: BankAccountComponent,
  data: {
    pageTitle: 'bankAccount.title',
  },
};
