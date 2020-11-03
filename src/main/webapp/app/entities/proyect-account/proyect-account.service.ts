import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IProyectAccount } from 'app/shared/model/proyect-account.model';

type EntityResponseType = HttpResponse<IProyectAccount>;
type EntityArrayResponseType = HttpResponse<IProyectAccount[]>;

@Injectable({ providedIn: 'root' })
export class ProyectAccountService {
  public resourceUrl = SERVER_API_URL + 'api/proyect-accounts';

  constructor(protected http: HttpClient) {}

  create(proyectAccount: IProyectAccount): Observable<EntityResponseType> {
    return this.http.post<IProyectAccount>(this.resourceUrl, proyectAccount, { observe: 'response' });
  }

  update(proyectAccount: IProyectAccount): Observable<EntityResponseType> {
    return this.http.put<IProyectAccount>(this.resourceUrl, proyectAccount, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProyectAccount>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProyectAccount[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
