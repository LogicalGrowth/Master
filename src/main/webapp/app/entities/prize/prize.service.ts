import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPrize } from 'app/shared/model/prize.model';

type EntityResponseType = HttpResponse<IPrize>;
type EntityArrayResponseType = HttpResponse<IPrize[]>;

@Injectable({ providedIn: 'root' })
export class PrizeService {
  public resourceUrl = SERVER_API_URL + 'api/prizes';

  constructor(protected http: HttpClient) {}

  create(prize: IPrize): Observable<EntityResponseType> {
    return this.http.post<IPrize>(this.resourceUrl, prize, { observe: 'response' });
  }

  update(prize: IPrize): Observable<EntityResponseType> {
    return this.http.put<IPrize>(this.resourceUrl, prize, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPrize>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPrize[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
