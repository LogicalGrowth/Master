import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICheckpoint, Checkpoint } from 'app/shared/model/checkpoint.model';
import { CheckpointService } from './checkpoint.service';
import { CheckpointComponent } from './checkpoint.component';
import { CheckpointDetailComponent } from './checkpoint-detail.component';
import { CheckpointUpdateComponent } from './checkpoint-update.component';
import { ProyectResolve } from '../proyect/proyect.route';

@Injectable({ providedIn: 'root' })
export class CheckpointResolve implements Resolve<ICheckpoint> {
  constructor(private service: CheckpointService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICheckpoint> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((checkpoint: HttpResponse<Checkpoint>) => {
          if (checkpoint.body) {
            return of(checkpoint.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Checkpoint());
  }
}

export const checkpointRoute: Routes = [
  {
    path: '',
    component: CheckpointComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.checkpoint.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CheckpointDetailComponent,
    resolve: {
      checkpoint: CheckpointResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.checkpoint.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/new',
    component: CheckpointUpdateComponent,
    resolve: {
      currentProyect: ProyectResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.checkpoint.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CheckpointUpdateComponent,
    resolve: {
      checkpoint: CheckpointResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'fun4FundApp.checkpoint.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
