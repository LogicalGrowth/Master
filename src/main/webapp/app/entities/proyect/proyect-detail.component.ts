import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IExclusiveContent } from 'app/shared/model/exclusive-content.model';

import { IProyect } from 'app/shared/model/proyect.model';
import { CheckpointService } from '../checkpoint/checkpoint.service';
import { PaymentService } from '../payment/payment.service';
import { ReviewService } from '../review/review.service';
import { ExclusiveContentService } from '../exclusive-content/exclusive-content.service';
import { AccountService } from 'app/core/auth/account.service';
import { User } from 'app/core/user/user.model';
import { ActivityStatus } from 'app/shared/model/enumerations/activity-status.model';
import * as moment from 'moment';

@Component({
  selector: 'jhi-proyect-detail',
  templateUrl: './proyect-detail.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss', '../../../content/scss/paper-dashboard/rating/rating.scss'],
})
export class ProyectDetailComponent implements OnInit {
  proyect: IProyect | null = null;
  reviews: any;
  ratings = [1, 2, 3, 4, 5];
  position: any[] = [];
  hasMarker = false;
  percentile: any;
  rating: any;
  donors: any;
  checkpoints: any;
  cards: any[] = [];
  exclusiveContents?: IExclusiveContent[];
  account!: User;
  isProjectOwner!: Boolean;
  daysCreated: any;
  updatedDays: any;

  constructor(
    protected activatedRoute: ActivatedRoute,
    private reviewService: ReviewService,
    private paymentService: PaymentService,
    private checkPointService: CheckpointService,
    protected exclusiveContentService: ExclusiveContentService,
    private accountService: AccountService
  ) {}

  loadExclusiveContent(projectId: number): void {
    if (this.proyect != null) {
      if (this.isProjectOwner) {
        this.exclusiveContentService
          .query({ 'proyectId.equals': projectId })
          .subscribe((res: HttpResponse<IExclusiveContent[]>) => (this.exclusiveContents = res.body || []));
      } else {
        this.exclusiveContentService
          .query({ 'proyectId.equals': projectId, 'stock.greaterThan': 1, 'state.equals': ActivityStatus.ENABLED })
          .subscribe((res: HttpResponse<IExclusiveContent[]>) => (this.exclusiveContents = res.body || []));
      }
    }
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proyect }) => {
      this.proyect = proyect;
      this.position.push(proyect.coordY);
      this.position.push(proyect.coordX);
      this.hasMarker = true;
      this.percentile = (100 * proyect.collected) / proyect.goalAmount;
      this.rating = (100 * proyect.rating) / 5;
      this.daysCreated = moment().diff(proyect.creationDate, 'days');
      this.updatedDays = moment().diff(proyect.lastUpdated, 'days');
      this.reviewService.findByProyect(proyect.id).subscribe(data => {
        this.reviews = data.body;
      });
      this.paymentService.findTopDonations(proyect.id).subscribe(data => {
        this.donors = data.body;
      });
      this.checkPointService.findByProyectId(proyect.id, this.percentile).subscribe(data => {
        if (data.body) {
          let i = 0;
          for (const checkpoint of data.body) {
            i++;
            const obj = {
              inverted: true,
              type: 'success',
              icon: 'nc-icon nc-sun-fog-29',
              subTitle: 'Checkpoint ' + i,
              body: checkpoint.message,
            };
            this.cards.push(obj);
          }
        }
      });
    });

    this.accountService.identity().subscribe(account => {
      if (account) {
        this.account = account;
      }
    });

    this.isProjectOwner = this.account.id === this.proyect?.owner?.id ? true : false;

    this.loadExclusiveContent(this.proyect?.id as number);
  }
}
