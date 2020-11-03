import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAuction } from 'app/shared/model/auction.model';

type EntityResponseType = HttpResponse<IAuction>;
type EntityArrayResponseType = HttpResponse<IAuction[]>;

@Injectable({ providedIn: 'root' })
export class AuctionService {
  public resourceUrl = SERVER_API_URL + 'api/auctions';

  constructor(protected http: HttpClient) {}

  create(auction: IAuction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(auction);
    return this.http
      .post<IAuction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(auction: IAuction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(auction);
    return this.http
      .put<IAuction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAuction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAuction[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(auction: IAuction): IAuction {
    const copy: IAuction = Object.assign({}, auction, {
      expirationDate: auction.expirationDate && auction.expirationDate.isValid() ? auction.expirationDate.toJSON() : undefined,
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
      res.body.forEach((auction: IAuction) => {
        auction.expirationDate = auction.expirationDate ? moment(auction.expirationDate) : undefined;
      });
    }
    return res;
  }
}
