import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { AccountService } from 'app/core/auth/account.service';
import { User } from 'app/core/user/user.model';
import { ApplicationUserService } from 'app/entities/application-user/application-user.service';
import { PaymentMethodService } from 'app/entities/payment-method/payment-method.service';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { CardType } from 'app/shared/model/enumerations/card-type.model';
import { IPaymentMethod, PaymentMethod } from 'app/shared/model/payment-method.model';
import * as moment from 'moment';
import { Observable } from 'rxjs';

@Component({
  selector: 'jhi-bank-account',
  templateUrl: './bank-account.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class BankAccountComponent implements OnInit {
  regexIbanCrc = /CR[a-zA-Z0-9]{2}\s?([0-9]{4}\s?){4}([0-9]{2})\s?/;
  editForm = this.fb.group({
    id: [],
    number: [null, Validators.compose([Validators.required, Validators.pattern(this.regexIbanCrc)])],
  });
  isSaving = false;
  account!: User;
  applicationUser?: IApplicationUser[];

  constructor(
    private fb: FormBuilder,
    protected paymentMethodService: PaymentMethodService,
    private accountService: AccountService,
    private applicationUserService: ApplicationUserService
  ) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      if (account) {
        this.account = account;

        this.applicationUserService
          .query({ 'internalUserId.equals': this.account.id })
          .subscribe((res: HttpResponse<IApplicationUser[]>) => {
            this.applicationUser = res.body || [];
            this.paymentMethodService
              .query({ 'ownerId.equals': this.applicationUser[0].id })
              .subscribe((res2: HttpResponse<IPaymentMethod[]>) => this.updateForm(res2.body![0]));
          });
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  updateForm(paymentMethod: IPaymentMethod): void {
    this.editForm.patchValue({
      id: paymentMethod.id,
      number: paymentMethod.cardOwner,
    });
  }

  save(): void {
    this.isSaving = true;
    const paymentMethod = this.createFromForm();
    if (paymentMethod.id) {
      this.subscribeToSaveResponse(this.paymentMethodService.update(paymentMethod));
    } else {
      this.subscribeToSaveResponse(this.paymentMethodService.create(paymentMethod));
    }
  }

  private createFromForm(): IPaymentMethod {
    return {
      ...new PaymentMethod(),
      id: this.editForm.get(['id'])!.value,
      cardNumber: this.editForm.get(['number'])!.value.replace('CR', '').slice(0, -1),
      cardOwner: this.editForm.get(['number'])!.value,
      expirationDate: moment(),
      type: CardType.VISA,
      cvc: '',
      favorite: false,
      owner: this.applicationUser![0],
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
