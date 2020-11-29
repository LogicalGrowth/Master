import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Fun4FundSharedModule } from 'app/shared/shared.module';

import { PasswordStrengthBarComponent } from './password/password-strength-bar.component';
import { ActivateComponent } from './activate/activate.component';
import { PasswordComponent } from './password/password.component';
import { PasswordResetInitComponent } from './password-reset/init/password-reset-init.component';
import { PasswordResetFinishComponent } from './password-reset/finish/password-reset-finish.component';
import { SettingsComponent } from './settings/settings.component';
import { accountState } from './account.route';
import { RegisterWizardComponent } from './register-wizard/register-wizard.component';
import { ComponentsModule } from 'app/components/components.module';
import { NgSelectModule } from '@ng-select/ng-select';
import { UserProfileComponent } from './user-profile/user-profile.component';

@NgModule({
  imports: [Fun4FundSharedModule, RouterModule.forChild(accountState), ComponentsModule, NgSelectModule],
  declarations: [
    ActivateComponent,
    PasswordComponent,
    PasswordStrengthBarComponent,
    PasswordResetInitComponent,
    PasswordResetFinishComponent,
    SettingsComponent,
    RegisterWizardComponent,
    UserProfileComponent,
  ],
})
export class AccountModule {}
