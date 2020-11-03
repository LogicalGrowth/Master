import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IDonationHistory, DonationHistory } from 'app/shared/model/donation-history.model';
import { DonationHistoryService } from './donation-history.service';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/application-user.service';
import { IProyect } from 'app/shared/model/proyect.model';
import { ProyectService } from 'app/entities/proyect/proyect.service';

type SelectableEntity = IApplicationUser | IProyect;

@Component({
  selector: 'jhi-donation-history-update',
  templateUrl: './donation-history-update.component.html',
})
export class DonationHistoryUpdateComponent implements OnInit {
  isSaving = false;
  applicationusers: IApplicationUser[] = [];
  proyects: IProyect[] = [];

  editForm = this.fb.group({
    id: [],
    amount: [null, [Validators.required, Validators.min(0)]],
    timeStamp: [null, [Validators.required]],
    applicationUser: [],
    proyect: [],
  });

  constructor(
    protected donationHistoryService: DonationHistoryService,
    protected applicationUserService: ApplicationUserService,
    protected proyectService: ProyectService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ donationHistory }) => {
      if (!donationHistory.id) {
        const today = moment().startOf('day');
        donationHistory.timeStamp = today;
      }

      this.updateForm(donationHistory);

      this.applicationUserService.query().subscribe((res: HttpResponse<IApplicationUser[]>) => (this.applicationusers = res.body || []));

      this.proyectService.query().subscribe((res: HttpResponse<IProyect[]>) => (this.proyects = res.body || []));
    });
  }

  updateForm(donationHistory: IDonationHistory): void {
    this.editForm.patchValue({
      id: donationHistory.id,
      amount: donationHistory.amount,
      timeStamp: donationHistory.timeStamp ? donationHistory.timeStamp.format(DATE_TIME_FORMAT) : null,
      applicationUser: donationHistory.applicationUser,
      proyect: donationHistory.proyect,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const donationHistory = this.createFromForm();
    if (donationHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.donationHistoryService.update(donationHistory));
    } else {
      this.subscribeToSaveResponse(this.donationHistoryService.create(donationHistory));
    }
  }

  private createFromForm(): IDonationHistory {
    return {
      ...new DonationHistory(),
      id: this.editForm.get(['id'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      timeStamp: this.editForm.get(['timeStamp'])!.value ? moment(this.editForm.get(['timeStamp'])!.value, DATE_TIME_FORMAT) : undefined,
      applicationUser: this.editForm.get(['applicationUser'])!.value,
      proyect: this.editForm.get(['proyect'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDonationHistory>>): void {
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
