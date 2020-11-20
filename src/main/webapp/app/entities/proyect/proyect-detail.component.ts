import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IExclusiveContent } from 'app/shared/model/exclusive-content.model';
import { IAuction } from 'app/shared/model/auction.model';

import { IProyect } from 'app/shared/model/proyect.model';
import { CheckpointService } from '../checkpoint/checkpoint.service';
import { PaymentService } from '../payment/payment.service';
import { ReviewService } from '../review/review.service';
import { ExclusiveContentService } from '../exclusive-content/exclusive-content.service';
import { AccountService } from 'app/core/auth/account.service';
import { User } from 'app/core/user/user.model';
import { ActivityStatus } from 'app/shared/model/enumerations/activity-status.model';
import * as moment from 'moment';
import { IResource } from 'app/shared/model/resource.model';
import { ResourceService } from '../resource/resource.service';
import { AuctionService } from '../auction/auction.service';

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
  mapCenter: any;
  hasMarker = false;
  percentile: any;
  rating: any;
  donors: any;
  checkpoints: any;
  cards: any[] = [];
  exclusiveContents?: IExclusiveContent[];
  auctions?: IAuction[];
  account!: User;
  isProjectOwner!: Boolean;
  daysCreated: any;
  updatedDays: any;
  items?: IResource[];
  css = `#gallery iframe{
    width: 100% !important;
    height: 31vw !important;
  }`;

  constructor(
    protected activatedRoute: ActivatedRoute,
    private reviewService: ReviewService,
    private paymentService: PaymentService,
    private checkPointService: CheckpointService,
    protected exclusiveContentService: ExclusiveContentService,
    protected auctionService: AuctionService,
    private accountService: AccountService,
    private resourceService: ResourceService
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

  loadAuction(projectId: number): void {
    if (this.proyect != null) {
      if (this.isProjectOwner) {
        this.auctionService
          .query({ 'proyectId.equals': projectId })
          .subscribe((res: HttpResponse<IAuction[]>) => (this.auctions = res.body || []));
      } else {
        this.auctionService
          .query({ 'proyectId.equals': projectId, 'state.equals': ActivityStatus.ENABLED })
          .subscribe((res: HttpResponse<IAuction[]>) => (this.auctions = res.body || []));
      }
    }
  }

  ngOnInit(): void {
    const style = document.createElement('style');
    style.innerHTML = this.css;
    document.head.appendChild(style);
    this.activatedRoute.data.subscribe(({ proyect }) => {
      this.proyect = proyect;
      this.position.push(proyect.coordY);
      this.position.push(proyect.coordX);
      this.mapCenter = {
        lat: proyect.coordY,
        lng: proyect.coordX,
      };
      this.hasMarker = true;
      this.percentile = (100 * proyect.collected) / proyect.goalAmount;
      this.rating = (100 * proyect.rating) / 5;
      this.daysCreated = moment().diff(proyect.creationDate, 'days');
      this.updatedDays = moment().diff(proyect.lastUpdated, 'days');
      this.reviewService.findByProyect(proyect.id).subscribe(data => {
        this.reviews = data.body;
      });
      this.resourceService
        .query({ 'proyectId.equals': proyect.id })
        .subscribe((res: HttpResponse<IResource[]>) => (this.items = res.body || []));
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
    this.loadAuction(this.proyect?.id as number);
  }
}
