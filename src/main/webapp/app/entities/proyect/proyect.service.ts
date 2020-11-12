import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IProyect } from 'app/shared/model/proyect.model';

type EntityResponseType = HttpResponse<IProyect>;
type EntityArrayResponseType = HttpResponse<IProyect[]>;

@Injectable({ providedIn: 'root' })
export class ProyectService {
  public resourceUrl = SERVER_API_URL + 'api/proyects';

  constructor(protected http: HttpClient) {}

  create(proyect: IProyect): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(proyect);
    return this.http
      .post<IProyect>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(proyect: IProyect): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(proyect);
    return this.http
      .put<IProyect>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProyect>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProyect[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(proyect: IProyect): IProyect {
    const copy: IProyect = Object.assign({}, proyect, {
      creationDate: proyect.creationDate && proyect.creationDate.isValid() ? proyect.creationDate.toJSON() : undefined,
      lastUpdated: proyect.lastUpdated && proyect.lastUpdated.isValid() ? proyect.lastUpdated.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creationDate = res.body.creationDate ? moment(res.body.creationDate) : undefined;
      res.body.lastUpdated = res.body.lastUpdated ? moment(res.body.lastUpdated) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((proyect: IProyect) => {
        proyect.creationDate = proyect.creationDate ? moment(proyect.creationDate) : undefined;
        proyect.lastUpdated = proyect.lastUpdated ? moment(proyect.lastUpdated) : undefined;
      });
    }
    return res;
  }

  public uploadImage(imagen: File, id: any): Observable<any> {
    const formData = new FormData();
    formData.append('multipartFile', imagen);
    return this.http.post<any>('api/cloudinary/uploadtoproyect/' + id, formData);
  }
}
