import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAuction } from 'app/shared/model/auction.model';
import { IResource } from '../../shared/model/resource.model';
import { map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ResourceService } from '../resource/resource.service';
import { IPrize } from '../../shared/model/prize.model';

@Component({
  selector: 'jhi-auction-detail',
  templateUrl: './auction-detail.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class AuctionDetailComponent implements OnInit {
  auction: IAuction | null = null;
  images: IResource[] = [];
  prize?: IPrize | null;

  constructor(protected activatedRoute: ActivatedRoute, protected resourceService: ResourceService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ auction }) => {
      if (auction) {
        this.auction = auction;
        this.prize = auction.prize;
      }
    });

    this.getImages(this.prize?.id as number);
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
