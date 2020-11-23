import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IAuction, Auction } from 'app/shared/model/auction.model';
import { AuctionService } from './auction.service';
import { IPrize, Prize } from 'app/shared/model/prize.model';
import { PrizeService } from 'app/entities/prize/prize.service';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/application-user.service';
import { IProyect } from 'app/shared/model/proyect.model';
import { ProyectService } from 'app/entities/proyect/proyect.service';
import { IResource, Resource } from '../../shared/model/resource.model';
import { ResourceService } from '../resource/resource.service';

type SelectableEntity = IPrize | IApplicationUser | IProyect;

@Component({
  selector: 'jhi-auction-update',
  templateUrl: './auction-update.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class AuctionUpdateComponent implements OnInit {
  creating = true;
  isSaving = false;
  prizes: IPrize[] = [];
  proyect?: IProyect;
  images: IResource[] = [];
  prize?: IPrize | null;
  imageSrc = '';

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [null, [Validators.required]],
    initialBid: [null, [Validators.required, Validators.min(0)]],
    expirationDate: [null, [Validators.required]],
    state: [null, [Validators.required]],
    prize: [],
    winner: [],
    proyect: [],
  });

  constructor(
    protected auctionService: AuctionService,
    protected prizeService: PrizeService,
    protected applicationUserService: ApplicationUserService,
    protected proyectService: ProyectService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    protected resourceService: ResourceService
  ) {}

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

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ auction }) => {
      if (auction) {
        this.creating = false;
        this.updateForm(auction);
        this.prize = auction.prize;
        this.proyect = auction.proyect;

        this.prizeService
          .query({ 'auctionId.specified': 'false' })
          .pipe(
            map((res: HttpResponse<IPrize[]>) => {
              return res.body || [];
            })
          )
          .subscribe((resBody: IPrize[]) => {
            if (!auction.prize || !auction.prize.id) {
              this.prizes = resBody;
            } else {
              this.prizeService
                .find(auction.prize.id)
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

    this.activatedRoute.data.subscribe(({ currentProyect }) => {
      if (currentProyect) {
        this.editForm.patchValue({
          id: undefined,
          proyect: currentProyect,
        });
      }
    });

    this.getImages(this.prize?.id as number);
  }

  updateForm(auction: IAuction): void {
    this.editForm.patchValue({
      id: auction.id,
      image: auction.prize?.images,
      name: auction.prize?.name,
      description: auction.prize?.description,
      initialBid: auction.initialBid,
      expirationDate: auction.expirationDate ? auction.expirationDate.format(DATE_TIME_FORMAT) : null,
      state: auction.state,
      prize: auction.prize,
      proyect: auction.proyect,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    let auction: IAuction;
    if (this.creating) {
      auction = this.createFromForm();
    } else {
      auction = this.updateFromForm();
    }
    if (auction.id) {
      this.subscribeToSaveResponse(this.auctionService.update(auction));
    } else {
      this.subscribeToSaveResponse(this.auctionService.create(auction));
    }
  }

  private createFromForm(): IAuction {
    const newPrize = {
      ...new Prize(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      images: this.images,
    };

    return {
      ...new Auction(),
      id: this.editForm.get(['id'])!.value,
      initialBid: this.editForm.get(['initialBid'])!.value,
      expirationDate: this.editForm.get(['expirationDate'])!.value
        ? moment(this.editForm.get(['expirationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      state: this.editForm.get(['state'])!.value,
      prize: newPrize,
      proyect: this.editForm.get(['proyect'])!.value,
    };
  }

  private updateFromForm(): IAuction {
    const newPrize = {
      ...new Prize(),
      id: this.prize?.id,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      images: this.images,
    };

    return {
      ...new Auction(),
      id: this.editForm.get(['id'])!.value,
      initialBid: this.editForm.get(['initialBid'])!.value,
      expirationDate: this.editForm.get(['expirationDate'])!.value
        ? moment(this.editForm.get(['expirationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      state: this.editForm.get(['state'])!.value,
      prize: newPrize,
      proyect: this.proyect!,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAuction>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  saveImage(data: any): void {
    this.imageSrc = data.secure_url;
    const newResource = {
      ...new Resource(),
      id: undefined,
      url: data.secure_url,
      type: 'Image',
      proyect: undefined,
      prize: undefined,
    };

    this.images?.push(newResource);
  }
}
