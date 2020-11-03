import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAdminPreferences } from 'app/shared/model/admin-preferences.model';

type EntityResponseType = HttpResponse<IAdminPreferences>;
type EntityArrayResponseType = HttpResponse<IAdminPreferences[]>;

@Injectable({ providedIn: 'root' })
export class AdminPreferencesService {
  public resourceUrl = SERVER_API_URL + 'api/admin-preferences';

  constructor(protected http: HttpClient) {}

  create(adminPreferences: IAdminPreferences): Observable<EntityResponseType> {
    return this.http.post<IAdminPreferences>(this.resourceUrl, adminPreferences, { observe: 'response' });
  }

  update(adminPreferences: IAdminPreferences): Observable<EntityResponseType> {
    return this.http.put<IAdminPreferences>(this.resourceUrl, adminPreferences, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAdminPreferences>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAdminPreferences[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
