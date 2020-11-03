import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPasswordHistory } from 'app/shared/model/password-history.model';

type EntityResponseType = HttpResponse<IPasswordHistory>;
type EntityArrayResponseType = HttpResponse<IPasswordHistory[]>;

@Injectable({ providedIn: 'root' })
export class PasswordHistoryService {
  public resourceUrl = SERVER_API_URL + 'api/password-histories';

  constructor(protected http: HttpClient) {}

  create(passwordHistory: IPasswordHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(passwordHistory);
    return this.http
      .post<IPasswordHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(passwordHistory: IPasswordHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(passwordHistory);
    return this.http
      .put<IPasswordHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPasswordHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPasswordHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(passwordHistory: IPasswordHistory): IPasswordHistory {
    const copy: IPasswordHistory = Object.assign({}, passwordHistory, {
      startDate: passwordHistory.startDate && passwordHistory.startDate.isValid() ? passwordHistory.startDate.toJSON() : undefined,
      endDate: passwordHistory.endDate && passwordHistory.endDate.isValid() ? passwordHistory.endDate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? moment(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? moment(res.body.endDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((passwordHistory: IPasswordHistory) => {
        passwordHistory.startDate = passwordHistory.startDate ? moment(passwordHistory.startDate) : undefined;
        passwordHistory.endDate = passwordHistory.endDate ? moment(passwordHistory.endDate) : undefined;
      });
    }
    return res;
  }
}
