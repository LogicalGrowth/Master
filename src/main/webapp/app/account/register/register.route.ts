import { Route } from '@angular/router';
import { RegisterWizardComponent } from '../register-wizard/register-wizard.component';

export const registerRoute: Route = {
  path: 'register-wizard',
  component: RegisterWizardComponent,
  data: {
    authorities: [],
    pageTitle: 'register.title',
  },
};
