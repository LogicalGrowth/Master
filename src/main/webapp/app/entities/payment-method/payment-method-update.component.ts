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
import { AccountService } from 'app/core/auth/account.service';
import { User } from 'app/core/user/user.model';

@Component({
  selector: 'jhi-payment-method-update',
  templateUrl: './payment-method-update.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss', './payment-method.scss'],
})
export class PaymentMethodUpdateComponent implements OnInit {
  isSaving = false;
  applicationusers: IApplicationUser[] = [];
  isValid = true;
  cardTypeImage = '';
  showCardImage = false;
  todayDate = new Date();
  type = '';
  account!: User;
  idUser = '';
  favorite = false;
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
    private fb: FormBuilder,
    private accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      if (account) {
        this.account = account;
        this.idUser = this.account.id;
      }
    });

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
      favorite: this.favorite,
      owner: {
        id: this.editForm.get(['owner'])!.value,
      },
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

  validateCard(x: any): any {
    if (this.validateCreditCardNumber(x.target.value)) {
      const type = this.cardType(x.target.value);
      const imgSrc = this.setCardImage(type);

      if (imgSrc !== '') {
        this.cardTypeImage = imgSrc;
        this.showCardImage = true;
        this.isValid = true;
      } else {
        this.isValid = false;
        this.cardTypeImage = '';
        this.showCardImage = false;
      }
    } else {
      this.isValid = false;
      this.cardTypeImage = '';
      this.showCardImage = false;
    }
  }

  validateCreditCardNumber(cardNumber: string): any {
    cardNumber = cardNumber.split(' ').join('');
    if (Number(cardNumber) <= 0 || !/\d{15,16}(~W[a-zA-Z])*$/.test(cardNumber) || cardNumber.length > 16) {
      return false;
    }
    const carray = [];
    for (let i = 0; i < cardNumber.length; i++) {
      carray[carray.length] = cardNumber.charCodeAt(i) - 48;
    }
    carray.reverse(); // luhn approaches number from the end
    let sum = 0;
    for (let i = 0; i < carray.length; i++) {
      let tmp = carray[i];
      if (i % 2 !== 0) {
        tmp *= 2;
        if (tmp > 9) {
          tmp -= 9;
        }
      }
      sum += tmp;
    }
    return sum % 10 === 0;
  }

  cardType(cardNumber: string): any {
    cardNumber = cardNumber.split(' ').join('');
    const o = {
      Visa: /^4[0-9]{12}(?:[0-9]{3})?$/,
      Mastercard: /^5[1-5][0-9]{14}$/,
      American: /^3[47]\d{13,14}$/,
    };
    for (const k in o) {
      if (o[k].test(cardNumber)) {
        return k;
      }
    }
    return null;
  }

  setCardImage(cardType: string): any {
    if (cardType === 'Visa') {
      this.type = 'VISA';
      return '../../../content/images/CardTypes/Visa.png';
    } else if (cardType === 'American') {
      this.type = 'EXPRESS';
      return '../../../content/images/CardTypes/Express.png';
    } else if (cardType === 'Mastercard') {
      this.type = 'MASTERCARD';
      return '../../../content/images/CardTypes/Mastercard.png';
    }

    return '';
  }

  isDateValid(date: any): any {
    if (date == null || date.value == null) {
      return false;
    }
    const dateValue = new Date(date.value);

    if (this.todayDate.getMonth() >= dateValue.getMonth() + 1) {
      if (this.todayDate.getFullYear() >= dateValue.getFullYear()) {
        return false;
      }
    }

    return true;
  }

  isValidCvc(cvc: any): any {
    if (cvc.value == null || cvc.value.length < 3 || cvc.value.length > 4 || cvc.value.length === 0) {
      return false;
    }
    return true;
  }
}
