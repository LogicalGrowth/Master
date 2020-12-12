import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IRaffle, Raffle } from 'app/shared/model/raffle.model';
import { RaffleService } from './raffle.service';
import { IPrize, Prize } from 'app/shared/model/prize.model';
import { PrizeService } from 'app/entities/prize/prize.service';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/application-user.service';
import { IProyect } from 'app/shared/model/proyect.model';
import { ProyectService } from 'app/entities/proyect/proyect.service';
import { Resource, IResource } from 'app/shared/model/resource.model';
import { ResourceService } from '../resource/resource.service';
import { ActivityStatus } from 'app/shared/model/enumerations/activity-status.model';

type SelectableEntity = IPrize | IApplicationUser | IProyect;

@Component({
  selector: 'jhi-raffle-update',
  templateUrl: './raffle-update.component.html',
})
export class RaffleUpdateComponent implements OnInit {
  creating = true;
  isSaving = false;
  prizes: IPrize[] = [];
  images: IResource[] = [];
  prize?: IPrize | null;
  applicationusers: IApplicationUser[] = [];
  proyects: IProyect[] = [];
  imageSrc = '';
  proyect?: IProyect;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    price: [null, [Validators.required, Validators.min(0)]],
    description: [null, [Validators.required]],
    totalTicket: [null, [Validators.required, Validators.min(1)]],
    expirationDate: [null, [Validators.required]],
    state: [],
    prize: [],
    buyer: [],
    proyect: [],
  });

  constructor(
    protected raffleService: RaffleService,
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
    this.activatedRoute.data.subscribe(({ raffle }) => {
      if (raffle) {
        this.creating = false;
        this.updateForm(raffle);
        this.prize = raffle.prize;
        this.proyect = raffle.proyect;

        this.prizeService
          .query({ 'raffleId.specified': 'false' })
          .pipe(
            map((res: HttpResponse<IPrize[]>) => {
              return res.body || [];
            })
          )
          .subscribe((resBody: IPrize[]) => {
            if (!raffle.prize || !raffle.prize.id) {
              this.prizes = resBody;
            } else {
              this.prizeService
                .find(raffle.prize.id)
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

  updateForm(raffle: IRaffle): void {
    this.editForm.patchValue({
      id: raffle.id,
      image: raffle.prize?.images,
      name: raffle.prize?.name,
      description: raffle.prize?.description,
      price: raffle.price,
      totalTicket: raffle.totalTicket,
      expirationDate: raffle.expirationDate ? raffle.expirationDate.format(DATE_TIME_FORMAT) : null,
      state: raffle.state,
      prize: raffle.prize,
      buyer: raffle.buyer,
      proyect: raffle.proyect,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    let raffle: IRaffle;
    if (this.creating) {
      raffle = this.createFromForm();
    } else {
      raffle = this.updateFromForm();
    }
    if (raffle.id !== undefined) {
      this.subscribeToSaveResponse(this.raffleService.update(raffle));
    } else {
      this.subscribeToSaveResponse(this.raffleService.create(raffle));
    }
  }

  private createFromForm(): IRaffle {
    const newPrize = {
      ...new Prize(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      images: this.images,
    };

    return {
      ...new Raffle(),
      id: this.editForm.get(['id'])!.value,
      price: this.editForm.get(['price'])!.value,

      totalTicket: this.editForm.get(['totalTicket'])!.value,
      expirationDate: this.editForm.get(['expirationDate'])!.value
        ? moment(this.editForm.get(['expirationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      state: ActivityStatus.ENABLED,
      prize: newPrize,
      buyer: this.editForm.get(['buyer'])!.value,
      proyect: this.editForm.get(['proyect'])!.value,
    };
  }
  private updateFromForm(): IRaffle {
    const newPrize = {
      ...new Prize(),
      id: this.prize?.id,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      images: this.images,
    };
    return {
      ...new Raffle(),
      id: this.editForm.get(['id'])!.value,
      price: this.editForm.get(['price'])!.value,
      totalTicket: this.editForm.get(['totalTicket'])!.value,
      expirationDate: this.editForm.get(['expirationDate'])!.value
        ? moment(this.editForm.get(['expirationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      state: this.editForm.get(['state'])!.value,
      prize: newPrize,
      proyect: this.proyect!,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRaffle>>): void {
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
