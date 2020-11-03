import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPaymentMethod, PaymentMethod } from 'app/shared/model/payment-method.model';
import { PaymentMethodService } from './payment-method.service';
import { PaymentMethodComponent } from './payment-method.component';
import { PaymentMethodDetailComponent } from './payment-method-detail.component';
import { PaymentMethodUpdateComponent } from './payment-method-update.component';

@Injectable({ providedIn: 'root' })
export class PaymentMethodResolve implements Resolve<IPaymentMethod> {
  constructor(private service: PaymentMethodService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPaymentMethod> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((paymentMethod: HttpResponse<PaymentMethod>) => {
          if (paymentMethod.body) {
            return of(paymentMethod.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PaymentMethod());
  }
}

export const paymentMethodRoute: Routes = [
  {
    path: '',
    component: PaymentMethodComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.paymentMethod.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaymentMethodDetailComponent,
    resolve: {
      paymentMethod: PaymentMethodResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.paymentMethod.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaymentMethodUpdateComponent,
    resolve: {
      paymentMethod: PaymentMethodResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.paymentMethod.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaymentMethodUpdateComponent,
    resolve: {
      paymentMethod: PaymentMethodResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.paymentMethod.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
