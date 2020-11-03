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
import { IPrize } from 'app/shared/model/prize.model';
import { PrizeService } from 'app/entities/prize/prize.service';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/application-user.service';
import { IProyect } from 'app/shared/model/proyect.model';
import { ProyectService } from 'app/entities/proyect/proyect.service';

type SelectableEntity = IPrize | IApplicationUser | IProyect;

@Component({
  selector: 'jhi-auction-update',
  templateUrl: './auction-update.component.html',
})
export class AuctionUpdateComponent implements OnInit {
  isSaving = false;
  prizes: IPrize[] = [];
  winners: IApplicationUser[] = [];
  proyects: IProyect[] = [];

  editForm = this.fb.group({
    id: [],
    initialBid: [null, [Validators.required, Validators.min(0)]],
    winningBid: [null, [Validators.min(0)]],
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
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ auction }) => {
      if (!auction.id) {
        const today = moment().startOf('day');
        auction.expirationDate = today;
      }

      this.updateForm(auction);

      this.prizeService
        .query({ filter: 'auction-is-null' })
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

      this.applicationUserService
        .query({ filter: 'auction-is-null' })
        .pipe(
          map((res: HttpResponse<IApplicationUser[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IApplicationUser[]) => {
          if (!auction.winner || !auction.winner.id) {
            this.winners = resBody;
          } else {
            this.applicationUserService
              .find(auction.winner.id)
              .pipe(
                map((subRes: HttpResponse<IApplicationUser>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IApplicationUser[]) => (this.winners = concatRes));
          }
        });

      this.proyectService.query().subscribe((res: HttpResponse<IProyect[]>) => (this.proyects = res.body || []));
    });
  }

  updateForm(auction: IAuction): void {
    this.editForm.patchValue({
      id: auction.id,
      initialBid: auction.initialBid,
      winningBid: auction.winningBid,
      expirationDate: auction.expirationDate ? auction.expirationDate.format(DATE_TIME_FORMAT) : null,
      state: auction.state,
      prize: auction.prize,
      winner: auction.winner,
      proyect: auction.proyect,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const auction = this.createFromForm();
    if (auction.id !== undefined) {
      this.subscribeToSaveResponse(this.auctionService.update(auction));
    } else {
      this.subscribeToSaveResponse(this.auctionService.create(auction));
    }
  }

  private createFromForm(): IAuction {
    return {
      ...new Auction(),
      id: this.editForm.get(['id'])!.value,
      initialBid: this.editForm.get(['initialBid'])!.value,
      winningBid: this.editForm.get(['winningBid'])!.value,
      expirationDate: this.editForm.get(['expirationDate'])!.value
        ? moment(this.editForm.get(['expirationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      state: this.editForm.get(['state'])!.value,
      prize: this.editForm.get(['prize'])!.value,
      winner: this.editForm.get(['winner'])!.value,
      proyect: this.editForm.get(['proyect'])!.value,
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
}
