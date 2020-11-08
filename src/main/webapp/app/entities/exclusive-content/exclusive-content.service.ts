import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IExclusiveContent } from 'app/shared/model/exclusive-content.model';

type EntityResponseType = HttpResponse<IExclusiveContent>;
type EntityArrayResponseType = HttpResponse<IExclusiveContent[]>;

@Injectable({ providedIn: 'root' })
export class ExclusiveContentService {
  public resourceUrl = SERVER_API_URL + 'api/exclusive-contents';

  constructor(protected http: HttpClient) {}

  create(exclusiveContent: IExclusiveContent): Observable<EntityResponseType> {
    return this.http.post<IExclusiveContent>(this.resourceUrl, exclusiveContent, { observe: 'response' });
  }

  update(exclusiveContent: IExclusiveContent): Observable<EntityResponseType> {
    return this.http.put<IExclusiveContent>(this.resourceUrl, exclusiveContent, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IExclusiveContent>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExclusiveContent[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
