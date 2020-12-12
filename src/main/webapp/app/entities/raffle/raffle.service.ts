import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IRaffle } from 'app/shared/model/raffle.model';

type EntityResponseType = HttpResponse<IRaffle>;
type EntityArrayResponseType = HttpResponse<IRaffle[]>;

@Injectable({ providedIn: 'root' })
export class RaffleService {
  public resourceUrl = SERVER_API_URL + 'api/raffles';

  constructor(protected http: HttpClient) {}

  create(raffle: IRaffle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(raffle);
    return this.http
      .post<IRaffle>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(raffle: IRaffle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(raffle);
    return this.http
      .put<IRaffle>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRaffle>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRaffle[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(raffle: IRaffle): IRaffle {
    const copy: IRaffle = Object.assign({}, raffle, {
      expirationDate: raffle.expirationDate && raffle.expirationDate.isValid() ? raffle.expirationDate.toJSON() : undefined,
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
      res.body.forEach((raffle: IRaffle) => {
        raffle.expirationDate = raffle.expirationDate ? moment(raffle.expirationDate) : undefined;
      });
    }
    return res;
  }

  getDataReportRaffle(numMonths = 6): Observable<EntityResponseType> {
    return this.http
      .get(`${this.resourceUrl}/getMyRaffleReport?numberMonths=${numMonths}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }
}
