import { Route } from '@angular/router';
import { UserProfileComponent } from './user-profile.component';

export const userProfileRoute: Route = {
  path: 'user-profile',
  component: UserProfileComponent,
  data: {
    authorities: [],
    pageTitle: 'userProfile.title',
  },
};
