import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IRetea } from 'app/shared/model/retea.model';

type EntityResponseType = HttpResponse<IRetea>;
type EntityArrayResponseType = HttpResponse<IRetea[]>;

@Injectable({ providedIn: 'root' })
export class ReteaService {
  public resourceUrl = SERVER_API_URL + 'api/reteas';

  constructor(protected http: HttpClient) {}

  create(retea: IRetea): Observable<EntityResponseType> {
    return this.http.post<IRetea>(this.resourceUrl, retea, { observe: 'response' });
  }

  update(retea: IRetea): Observable<EntityResponseType> {
    return this.http.put<IRetea>(this.resourceUrl, retea, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRetea>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRetea[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
