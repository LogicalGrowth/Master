import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDonationHistory } from 'app/shared/model/donation-history.model';

type EntityResponseType = HttpResponse<IDonationHistory>;
type EntityArrayResponseType = HttpResponse<IDonationHistory[]>;

@Injectable({ providedIn: 'root' })
export class DonationHistoryService {
  public resourceUrl = SERVER_API_URL + 'api/donation-histories';

  constructor(protected http: HttpClient) {}

  create(donationHistory: IDonationHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(donationHistory);
    return this.http
      .post<IDonationHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(donationHistory: IDonationHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(donationHistory);
    return this.http
      .put<IDonationHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDonationHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDonationHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(donationHistory: IDonationHistory): IDonationHistory {
    const copy: IDonationHistory = Object.assign({}, donationHistory, {
      timeStamp: donationHistory.timeStamp && donationHistory.timeStamp.isValid() ? donationHistory.timeStamp.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.timeStamp = res.body.timeStamp ? moment(res.body.timeStamp) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((donationHistory: IDonationHistory) => {
        donationHistory.timeStamp = donationHistory.timeStamp ? moment(donationHistory.timeStamp) : undefined;
      });
    }
    return res;
  }
}
