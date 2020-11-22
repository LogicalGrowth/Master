import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AccountService } from 'app/core/auth/account.service';
import { User } from 'app/core/user/user.model';
import { IApplicationUser } from 'app/shared/model/application-user.model';

import { IExclusiveContent } from 'app/shared/model/exclusive-content.model';
import { IPrize } from 'app/shared/model/prize.model';
import { IResource } from 'app/shared/model/resource.model';
import { map } from 'rxjs/operators';
import { ApplicationUserService } from '../application-user/application-user.service';
import { PrizeService } from '../prize/prize.service';
import { ResourceService } from '../resource/resource.service';

@Component({
  selector: 'jhi-exclusive-content-detail',
  templateUrl: './exclusive-content-detail.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class ExclusiveContentDetailComponent implements OnInit {
  exclusiveContent: IExclusiveContent | null = null;
  prizes: IPrize[] = [];
  prize: IPrize | undefined;
  images: IResource[] = [];
  account!: User;
  isProjectOwner!: Boolean;
  applicationUser?: IApplicationUser[];

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected resourceService: ResourceService,
    protected prizeService: PrizeService,
    private accountService: AccountService,
    private applicationUserService: ApplicationUserService
  ) {}

  getImages(exclusiveContentId: number): any {
    if (exclusiveContentId)
      this.resourceService
        .query({ 'prizeId.equals': exclusiveContentId })
        .pipe(
          map((res: HttpResponse<IResource[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IResource[]) => {
          this.images = resBody;
          // eslint-disable-next-line no-console
          console.log(this.images);
        });
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exclusiveContent }) => {
      if (exclusiveContent) {
        this.exclusiveContent = exclusiveContent;
        // eslint-disable-next-line no-console
        console.log(this.exclusiveContent);
        this.prize = exclusiveContent.prize;

        this.getImages(this.prize?.id as number);

        this.prizeService
          .query({ 'exclusiveContentId.specified': 'false' })
          .pipe(
            map((res: HttpResponse<IPrize[]>) => {
              return res.body || [];
            })
          )
          .subscribe((resBody: IPrize[]) => {
            if (!exclusiveContent.prize || !exclusiveContent.prize.id) {
              this.prizes = resBody;
            } else {
              this.prizeService
                .find(exclusiveContent.prize.id)
                .pipe(
                  map((subRes: HttpResponse<IPrize>) => {
                    return subRes.body ? [subRes.body].concat(resBody) : resBody;
                  })
                )
                .subscribe((concatRes: IPrize[]) => (this.prizes = concatRes));
            }
          });
      }
    });

    this.accountService.identity().subscribe(account => {
      if (account) {
        this.account = account;

        this.applicationUserService
          .query({ 'internalUserId.equals': this.account.id })
          .subscribe((res: HttpResponse<IApplicationUser[]>) => {
            this.applicationUser = res.body || [];
            this.isProjectOwner = this.applicationUser[0].id === this.exclusiveContent?.proyect?.owner?.id ? true : false;
          });
      }
    });
  }

  previousState(): void {
    window.history.back();
  }
}
