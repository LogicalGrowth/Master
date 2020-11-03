import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPartnerRequest } from 'app/shared/model/partner-request.model';

type EntityResponseType = HttpResponse<IPartnerRequest>;
type EntityArrayResponseType = HttpResponse<IPartnerRequest[]>;

@Injectable({ providedIn: 'root' })
export class PartnerRequestService {
  public resourceUrl = SERVER_API_URL + 'api/partner-requests';

  constructor(protected http: HttpClient) {}

  create(partnerRequest: IPartnerRequest): Observable<EntityResponseType> {
    return this.http.post<IPartnerRequest>(this.resourceUrl, partnerRequest, { observe: 'response' });
  }

  update(partnerRequest: IPartnerRequest): Observable<EntityResponseType> {
    return this.http.put<IPartnerRequest>(this.resourceUrl, partnerRequest, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPartnerRequest>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPartnerRequest[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
