import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICheckpoint } from 'app/shared/model/checkpoint.model';

type EntityResponseType = HttpResponse<ICheckpoint>;
type EntityArrayResponseType = HttpResponse<ICheckpoint[]>;

@Injectable({ providedIn: 'root' })
export class CheckpointService {
  public resourceUrl = SERVER_API_URL + 'api/checkpoints';

  constructor(protected http: HttpClient) {}

  create(checkpoint: ICheckpoint): Observable<EntityResponseType> {
    return this.http.post<ICheckpoint>(this.resourceUrl, checkpoint, { observe: 'response' });
  }

  update(checkpoint: ICheckpoint): Observable<EntityResponseType> {
    return this.http.put<ICheckpoint>(this.resourceUrl, checkpoint, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICheckpoint>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICheckpoint[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findByProyectId(id: number, percentile: number): Observable<EntityArrayResponseType> {
    return this.http.get<ICheckpoint[]>(`${this.resourceUrl}/byproyect?idproyect=${id}&percentile=${percentile}`, { observe: 'response' });
  }
}
