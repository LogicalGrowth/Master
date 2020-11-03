import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IPayment, Payment } from 'app/shared/model/payment.model';
import { PaymentService } from './payment.service';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/application-user.service';
import { IProyect } from 'app/shared/model/proyect.model';
import { ProyectService } from 'app/entities/proyect/proyect.service';

type SelectableEntity = IApplicationUser | IProyect;

@Component({
  selector: 'jhi-payment-update',
  templateUrl: './payment-update.component.html',
})
export class PaymentUpdateComponent implements OnInit {
  isSaving = false;
  applicationusers: IApplicationUser[] = [];
  proyects: IProyect[] = [];

  editForm = this.fb.group({
    id: [],
    amount: [null, [Validators.required, Validators.min(0)]],
    type: [null, [Validators.required]],
    timeStamp: [null, [Validators.required]],
    applicationUser: [],
    proyect: [],
  });

  constructor(
    protected paymentService: PaymentService,
    protected applicationUserService: ApplicationUserService,
    protected proyectService: ProyectService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ payment }) => {
      if (!payment.id) {
        const today = moment().startOf('day');
        payment.timeStamp = today;
      }

      this.updateForm(payment);

      this.applicationUserService.query().subscribe((res: HttpResponse<IApplicationUser[]>) => (this.applicationusers = res.body || []));

      this.proyectService.query().subscribe((res: HttpResponse<IProyect[]>) => (this.proyects = res.body || []));
    });
  }

  updateForm(payment: IPayment): void {
    this.editForm.patchValue({
      id: payment.id,
      amount: payment.amount,
      type: payment.type,
      timeStamp: payment.timeStamp ? payment.timeStamp.format(DATE_TIME_FORMAT) : null,
      applicationUser: payment.applicationUser,
      proyect: payment.proyect,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const payment = this.createFromForm();
    if (payment.id !== undefined) {
      this.subscribeToSaveResponse(this.paymentService.update(payment));
    } else {
      this.subscribeToSaveResponse(this.paymentService.create(payment));
    }
  }

  private createFromForm(): IPayment {
    return {
      ...new Payment(),
      id: this.editForm.get(['id'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      type: this.editForm.get(['type'])!.value,
      timeStamp: this.editForm.get(['timeStamp'])!.value ? moment(this.editForm.get(['timeStamp'])!.value, DATE_TIME_FORMAT) : undefined,
      applicationUser: this.editForm.get(['applicationUser'])!.value,
      proyect: this.editForm.get(['proyect'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPayment>>): void {
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
