import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IFavorite } from 'app/shared/model/favorite.model';
import { map } from 'rxjs/operators';

type EntityResponseType = HttpResponse<IFavorite>;
type EntityArrayResponseType = HttpResponse<IFavorite[]>;

@Injectable({ providedIn: 'root' })
export class FavoriteService {
  public resourceUrl = SERVER_API_URL + 'api/favorites';

  constructor(protected http: HttpClient) {}

  create(favorite: IFavorite): Observable<EntityResponseType> {
    return this.http.post<IFavorite>(this.resourceUrl, favorite, { observe: 'response' });
  }

  update(favorite: IFavorite): Observable<EntityResponseType> {
    return this.http.put<IFavorite>(this.resourceUrl, favorite, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFavorite>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFavorite[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    return res;
  }

  getTop5Favorites(): Observable<EntityResponseType> {
    return this.http
      .get(`${this.resourceUrl}/getTop5Favorites`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }
}
