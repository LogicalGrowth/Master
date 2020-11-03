import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IRecommendation, Recommendation } from 'app/shared/model/recommendation.model';
import { RecommendationService } from './recommendation.service';
import { RecommendationComponent } from './recommendation.component';
import { RecommendationDetailComponent } from './recommendation-detail.component';
import { RecommendationUpdateComponent } from './recommendation-update.component';

@Injectable({ providedIn: 'root' })
export class RecommendationResolve implements Resolve<IRecommendation> {
  constructor(private service: RecommendationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRecommendation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((recommendation: HttpResponse<Recommendation>) => {
          if (recommendation.body) {
            return of(recommendation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Recommendation());
  }
}

export const recommendationRoute: Routes = [
  {
    path: '',
    component: RecommendationComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.recommendation.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RecommendationDetailComponent,
    resolve: {
      recommendation: RecommendationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.recommendation.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RecommendationUpdateComponent,
    resolve: {
      recommendation: RecommendationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.recommendation.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RecommendationUpdateComponent,
    resolve: {
      recommendation: RecommendationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.recommendation.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
