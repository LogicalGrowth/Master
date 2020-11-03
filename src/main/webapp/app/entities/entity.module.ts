import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'image',
        loadChildren: () => import('./image/image.module').then(m => m.Fun4FundImageModule),
      },
      {
        path: 'application-user',
        loadChildren: () => import('./application-user/application-user.module').then(m => m.Fun4FundApplicationUserModule),
      },
      {
        path: 'payment-method',
        loadChildren: () => import('./payment-method/payment-method.module').then(m => m.Fun4FundPaymentMethodModule),
      },
      {
        path: 'proyect',
        loadChildren: () => import('./proyect/proyect.module').then(m => m.Fun4FundProyectModule),
      },
      {
        path: 'proyect-account',
        loadChildren: () => import('./proyect-account/proyect-account.module').then(m => m.Fun4FundProyectAccountModule),
      },
      {
        path: 'category',
        loadChildren: () => import('./category/category.module').then(m => m.Fun4FundCategoryModule),
      },
      {
        path: 'checkpoint',
        loadChildren: () => import('./checkpoint/checkpoint.module').then(m => m.Fun4FundCheckpointModule),
      },
      {
        path: 'review',
        loadChildren: () => import('./review/review.module').then(m => m.Fun4FundReviewModule),
      },
      {
        path: 'donation-history',
        loadChildren: () => import('./donation-history/donation-history.module').then(m => m.Fun4FundDonationHistoryModule),
      },
      {
        path: 'auction',
        loadChildren: () => import('./auction/auction.module').then(m => m.Fun4FundAuctionModule),
      },
      {
        path: 'raffle',
        loadChildren: () => import('./raffle/raffle.module').then(m => m.Fun4FundRaffleModule),
      },
      {
        path: 'exclusive-content',
        loadChildren: () => import('./exclusive-content/exclusive-content.module').then(m => m.Fun4FundExclusiveContentModule),
      },
      {
        path: 'prize',
        loadChildren: () => import('./prize/prize.module').then(m => m.Fun4FundPrizeModule),
      },
      {
        path: 'partner-request',
        loadChildren: () => import('./partner-request/partner-request.module').then(m => m.Fun4FundPartnerRequestModule),
      },
      {
        path: 'notification',
        loadChildren: () => import('./notification/notification.module').then(m => m.Fun4FundNotificationModule),
      },
      {
        path: 'payment',
        loadChildren: () => import('./payment/payment.module').then(m => m.Fun4FundPaymentModule),
      },
      {
        path: 'user-preferences',
        loadChildren: () => import('./user-preferences/user-preferences.module').then(m => m.Fun4FundUserPreferencesModule),
      },
      {
        path: 'admin-preferences',
        loadChildren: () => import('./admin-preferences/admin-preferences.module').then(m => m.Fun4FundAdminPreferencesModule),
      },
      {
        path: 'fee',
        loadChildren: () => import('./fee/fee.module').then(m => m.Fun4FundFeeModule),
      },
      {
        path: 'app-log',
        loadChildren: () => import('./app-log/app-log.module').then(m => m.Fun4FundAppLogModule),
      },
      {
        path: 'recommendation',
        loadChildren: () => import('./recommendation/recommendation.module').then(m => m.Fun4FundRecommendationModule),
      },
      {
        path: 'password-history',
        loadChildren: () => import('./password-history/password-history.module').then(m => m.Fun4FundPasswordHistoryModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class Fun4FundEntityModule {}
