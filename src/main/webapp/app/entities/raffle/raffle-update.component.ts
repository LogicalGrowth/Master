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
import { IPrize } from 'app/shared/model/prize.model';
import { PrizeService } from 'app/entities/prize/prize.service';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/application-user.service';
import { IProyect } from 'app/shared/model/proyect.model';
import { ProyectService } from 'app/entities/proyect/proyect.service';

type SelectableEntity = IPrize | IApplicationUser | IProyect;

@Component({
  selector: 'jhi-raffle-update',
  templateUrl: './raffle-update.component.html',
})
export class RaffleUpdateComponent implements OnInit {
  isSaving = false;
  prizes: IPrize[] = [];
  applicationusers: IApplicationUser[] = [];
  proyects: IProyect[] = [];

  editForm = this.fb.group({
    id: [],
    price: [null, [Validators.required, Validators.min(0)]],
    totalTicket: [null, [Validators.required, Validators.min(1)]],
    expirationDate: [null, [Validators.required]],
    state: [null, [Validators.required]],
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
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ raffle }) => {
      if (!raffle.id) {
        const today = moment().startOf('day');
        raffle.expirationDate = today;
      }

      this.updateForm(raffle);

      this.prizeService
        .query({ filter: 'raffle-is-null' })
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

      this.applicationUserService.query().subscribe((res: HttpResponse<IApplicationUser[]>) => (this.applicationusers = res.body || []));

      this.proyectService.query().subscribe((res: HttpResponse<IProyect[]>) => (this.proyects = res.body || []));
    });
  }

  updateForm(raffle: IRaffle): void {
    this.editForm.patchValue({
      id: raffle.id,
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
    const raffle = this.createFromForm();
    if (raffle.id !== undefined) {
      this.subscribeToSaveResponse(this.raffleService.update(raffle));
    } else {
      this.subscribeToSaveResponse(this.raffleService.create(raffle));
    }
  }

  private createFromForm(): IRaffle {
    return {
      ...new Raffle(),
      id: this.editForm.get(['id'])!.value,
      price: this.editForm.get(['price'])!.value,
      totalTicket: this.editForm.get(['totalTicket'])!.value,
      expirationDate: this.editForm.get(['expirationDate'])!.value
        ? moment(this.editForm.get(['expirationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      state: this.editForm.get(['state'])!.value,
      prize: this.editForm.get(['prize'])!.value,
      buyer: this.editForm.get(['buyer'])!.value,
      proyect: this.editForm.get(['proyect'])!.value,
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
}
