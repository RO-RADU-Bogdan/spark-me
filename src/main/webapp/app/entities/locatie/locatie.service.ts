import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ILocatie } from 'app/shared/model/locatie.model';

type EntityResponseType = HttpResponse<ILocatie>;
type EntityArrayResponseType = HttpResponse<ILocatie[]>;

@Injectable({ providedIn: 'root' })
export class LocatieService {
  public resourceUrl = SERVER_API_URL + 'api/locaties';

  constructor(protected http: HttpClient) {}

  create(locatie: ILocatie): Observable<EntityResponseType> {
    return this.http.post<ILocatie>(this.resourceUrl, locatie, { observe: 'response' });
  }

  update(locatie: ILocatie): Observable<EntityResponseType> {
    return this.http.put<ILocatie>(this.resourceUrl, locatie, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILocatie>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILocatie[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
