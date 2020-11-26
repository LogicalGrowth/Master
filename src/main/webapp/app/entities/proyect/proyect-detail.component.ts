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
import { ICheckpoint } from 'app/shared/model/checkpoint.model';
import { ApplicationUserService } from '../application-user/application-user.service';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { ProductType } from 'app/shared/model/enumerations/product-type.model';
import { DonationModalService } from './donation/donationModal.service';
import { BidModalService } from '../auction/bid/bidModal.service';

@Component({
  selector: 'jhi-proyect-detail',
  templateUrl: './proyect-detail.component.html',
  styleUrls: [
    '../../../content/scss/paper-dashboard.scss',
    '../../../content/scss/paper-dashboard/rating/rating.scss',
    'proyecto-detail.scss',
  ],
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
  applicationUser?: IApplicationUser[];
  productType?: ProductType;
  userId: any;

  constructor(
    protected activatedRoute: ActivatedRoute,
    private reviewService: ReviewService,
    private paymentService: PaymentService,
    private checkPointService: CheckpointService,
    protected exclusiveContentService: ExclusiveContentService,
    protected auctionService: AuctionService,
    private accountService: AccountService,
    private resourceService: ResourceService,
    private applicationUserService: ApplicationUserService,
    private donationModalService: DonationModalService,
    private bidModalService: BidModalService
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

  pushCardsCheckpoints(data: any): void {
    if (data.body) {
      let i = 0;
      for (const checkpoint of data.body) {
        i++;
        const obj = {
          inverted: true,
          type: 'info',
          icon: 'nc-icon nc-check-2',
          subTitle: 'Checkpoint ' + i + ' de ' + checkpoint.completitionPercentage + '%',
          body: checkpoint.message,
          isOwner: this.isProjectOwner,
          routerLink: '/checkpoint/' + checkpoint.id + '/edit',
        };
        this.cards.push(obj);
      }
    }
  }

  loadCheckPoints(projectId: number): void {
    if (this.proyect != null) {
      if (this.isProjectOwner) {
        this.checkPointService
          .query({ 'proyectId.equals': projectId })
          .subscribe((res: HttpResponse<ICheckpoint[]>) => this.pushCardsCheckpoints(res));
      } else {
        this.checkPointService.findByProyectId(projectId, this.percentile).subscribe(data => {
          this.pushCardsCheckpoints(data);
        });
      }
    }
  }

  ngOnInit(): void {
    this.productType = ProductType.DONATION;

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
      this.percentile = Math.floor((100 * proyect.collected) / proyect.goalAmount);
      this.rating = (100 * proyect.rating) / 5;
      this.daysCreated = moment().diff(proyect.creationDate, 'days');
      this.daysCreated = this.daysCreated === 0 ? 'Pocas horas ' : 'Hace ' + this.daysCreated + ' días ';
      this.updatedDays = moment().diff(proyect.lastUpdated, 'days');
      this.updatedDays = this.updatedDays === 0 ? 'Pocas horas ' : 'Hace ' + this.updatedDays + ' días ';
      this.reviewService.findByProyect(proyect.id).subscribe(data => {
        this.reviews = data.body;
      });
      this.resourceService
        .query({ 'proyectId.equals': proyect.id })
        .subscribe((res: HttpResponse<IResource[]>) => (this.items = res.body || []));
      this.paymentService.findTopDonations(proyect.id).subscribe(data => {
        this.donors = data.body;
      });
    });

    this.accountService.identity().subscribe(account => {
      if (account) {
        this.account = account;

        this.applicationUserService
          .query({ 'internalUserId.equals': this.account.id })
          .subscribe((res: HttpResponse<IApplicationUser[]>) => {
            this.applicationUser = res.body || [];
            this.userId = this.applicationUser[0].id;
            this.isProjectOwner = this.applicationUser[0].id === this.proyect?.owner?.id ? true : false;
            this.loadCheckPoints(this.proyect?.id as number);
            this.loadExclusiveContent(this.proyect?.id as number);
            this.loadAuction(this.proyect?.id as number);
          });
      }
    });
  }

  donate(): void {
    this.donationModalService.open(this.proyect!);
  }

  bid(auction: IAuction): void {
    this.bidModalService.open(auction);
  }
}
