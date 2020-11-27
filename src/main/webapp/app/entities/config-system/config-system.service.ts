import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IConfigSystem } from 'app/shared/model/config-system.model';

type EntityResponseType = HttpResponse<IConfigSystem>;
type EntityArrayResponseType = HttpResponse<IConfigSystem[]>;

@Injectable({ providedIn: 'root' })
export class ConfigSystemService {
  public resourceUrl = SERVER_API_URL + 'api/config-systems';

  constructor(protected http: HttpClient) {}

  create(configSystem: IConfigSystem): Observable<EntityResponseType> {
    return this.http.post<IConfigSystem>(this.resourceUrl, configSystem, { observe: 'response' });
  }

  update(configSystem: IConfigSystem): Observable<EntityResponseType> {
    return this.http.put<IConfigSystem>(this.resourceUrl, configSystem, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConfigSystem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConfigSystem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
