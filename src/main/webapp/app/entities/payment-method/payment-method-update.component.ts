import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IPaymentMethod, PaymentMethod } from 'app/shared/model/payment-method.model';
import { PaymentMethodService } from './payment-method.service';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/application-user.service';

@Component({
  selector: 'jhi-payment-method-update',
  templateUrl: './payment-method-update.component.html',
})
export class PaymentMethodUpdateComponent implements OnInit {
  isSaving = false;
  applicationusers: IApplicationUser[] = [];

  editForm = this.fb.group({
    id: [],
    cardNumber: [null, [Validators.required, Validators.minLength(14), Validators.maxLength(19)]],
    cardOwner: [null, [Validators.required]],
    expirationDate: [null, [Validators.required]],
    type: [null, [Validators.required]],
    cvc: [null, [Validators.required]],
    favorite: [null, [Validators.required]],
    owner: [],
  });

  constructor(
    protected paymentMethodService: PaymentMethodService,
    protected applicationUserService: ApplicationUserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentMethod }) => {
      if (!paymentMethod.id) {
        const today = moment().startOf('day');
        paymentMethod.expirationDate = today;
      }

      this.updateForm(paymentMethod);

      this.applicationUserService.query().subscribe((res: HttpResponse<IApplicationUser[]>) => (this.applicationusers = res.body || []));
    });
  }

  updateForm(paymentMethod: IPaymentMethod): void {
    this.editForm.patchValue({
      id: paymentMethod.id,
      cardNumber: paymentMethod.cardNumber,
      cardOwner: paymentMethod.cardOwner,
      expirationDate: paymentMethod.expirationDate ? paymentMethod.expirationDate.format(DATE_TIME_FORMAT) : null,
      type: paymentMethod.type,
      cvc: paymentMethod.cvc,
      favorite: paymentMethod.favorite,
      owner: paymentMethod.owner,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paymentMethod = this.createFromForm();
    if (paymentMethod.id !== undefined) {
      this.subscribeToSaveResponse(this.paymentMethodService.update(paymentMethod));
    } else {
      this.subscribeToSaveResponse(this.paymentMethodService.create(paymentMethod));
    }
  }

  private createFromForm(): IPaymentMethod {
    return {
      ...new PaymentMethod(),
      id: this.editForm.get(['id'])!.value,
      cardNumber: this.editForm.get(['cardNumber'])!.value,
      cardOwner: this.editForm.get(['cardOwner'])!.value,
      expirationDate: this.editForm.get(['expirationDate'])!.value
        ? moment(this.editForm.get(['expirationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      type: this.editForm.get(['type'])!.value,
      cvc: this.editForm.get(['cvc'])!.value,
      favorite: this.editForm.get(['favorite'])!.value,
      owner: this.editForm.get(['owner'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaymentMethod>>): void {
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

  trackById(index: number, item: IApplicationUser): any {
    return item.id;
  }
}
