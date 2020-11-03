import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAppLog } from 'app/shared/model/app-log.model';

type EntityResponseType = HttpResponse<IAppLog>;
type EntityArrayResponseType = HttpResponse<IAppLog[]>;

@Injectable({ providedIn: 'root' })
export class AppLogService {
  public resourceUrl = SERVER_API_URL + 'api/app-logs';

  constructor(protected http: HttpClient) {}

  create(appLog: IAppLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appLog);
    return this.http
      .post<IAppLog>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(appLog: IAppLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appLog);
    return this.http
      .put<IAppLog>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAppLog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAppLog[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(appLog: IAppLog): IAppLog {
    const copy: IAppLog = Object.assign({}, appLog, {
      timeStamp: appLog.timeStamp && appLog.timeStamp.isValid() ? appLog.timeStamp.toJSON() : undefined,
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
      res.body.forEach((appLog: IAppLog) => {
        appLog.timeStamp = appLog.timeStamp ? moment(appLog.timeStamp) : undefined;
      });
    }
    return res;
  }
}
