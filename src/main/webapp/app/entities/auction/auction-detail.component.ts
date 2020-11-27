import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAuction } from 'app/shared/model/auction.model';
import { IResource } from '../../shared/model/resource.model';
import { map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { ResourceService } from '../resource/resource.service';
import { IPrize } from '../../shared/model/prize.model';
import { AccountService } from 'app/core/auth/account.service';
import { ApplicationUserService } from '../application-user/application-user.service';
import { User } from 'app/core/user/user.model';
import { IApplicationUser } from 'app/shared/model/application-user.model';

@Component({
  selector: 'jhi-auction-detail',
  templateUrl: './auction-detail.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class AuctionDetailComponent implements OnInit {
  auction: IAuction | null = null;
  images: IResource[] = [];
  prize?: IPrize | null;
  account!: User;
  isProjectOwner!: Boolean;
  applicationUser?: IApplicationUser[];

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected resourceService: ResourceService,
    private accountService: AccountService,
    private applicationUserService: ApplicationUserService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ auction }) => {
      if (auction) {
        this.auction = auction;
        this.prize = auction.prize;
      }
    });

    this.getImages(this.prize?.id as number);

    this.accountService.identity().subscribe(account => {
      if (account) {
        this.account = account;

        this.applicationUserService
          .query({ 'internalUserId.equals': this.account.id })
          .subscribe((res: HttpResponse<IApplicationUser[]>) => {
            this.applicationUser = res.body || [];
            this.isProjectOwner = this.applicationUser[0].id === this.auction?.proyect?.owner?.id ? true : false;
          });
      }
    });
  }

  getImages(auctionId: number): any {
    if (auctionId)
      this.resourceService
        .query({ 'prizeId.equals': auctionId })
        .pipe(
          map((res: HttpResponse<IResource[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IResource[]) => {
          this.images = resBody;
        });
  }

  previousState(): void {
    window.history.back();
  }
}
