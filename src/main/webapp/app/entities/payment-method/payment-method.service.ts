import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPaymentMethod } from 'app/shared/model/payment-method.model';

type EntityResponseType = HttpResponse<IPaymentMethod>;
type EntityArrayResponseType = HttpResponse<IPaymentMethod[]>;

@Injectable({ providedIn: 'root' })
export class PaymentMethodService {
  public resourceUrl = SERVER_API_URL + 'api/payment-methods';

  constructor(protected http: HttpClient) {}

  create(paymentMethod: IPaymentMethod): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paymentMethod);
    return this.http
      .post<IPaymentMethod>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(paymentMethod: IPaymentMethod): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paymentMethod);
    return this.http
      .put<IPaymentMethod>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPaymentMethod>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPaymentMethod[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(paymentMethod: IPaymentMethod): IPaymentMethod {
    const copy: IPaymentMethod = Object.assign({}, paymentMethod, {
      expirationDate:
        paymentMethod.expirationDate && paymentMethod.expirationDate.isValid() ? paymentMethod.expirationDate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.expirationDate = res.body.expirationDate ? moment(res.body.expirationDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((paymentMethod: IPaymentMethod) => {
        paymentMethod.expirationDate = paymentMethod.expirationDate ? moment(paymentMethod.expirationDate) : undefined;
      });
    }
    return res;
  }
}
