import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IStatie } from 'app/shared/model/statie.model';

type EntityResponseType = HttpResponse<IStatie>;
type EntityArrayResponseType = HttpResponse<IStatie[]>;

@Injectable({ providedIn: 'root' })
export class StatieService {
  public resourceUrl = SERVER_API_URL + 'api/staties';

  //
  public listaStatiiUrl = SERVER_API_URL + 'api/listaStatii';
  public statieSelectata: IStatie = null;
  //

  constructor(protected http: HttpClient) {}

  // Gaseste toate statiile existente:
  getListaStatii(): Observable<IStatie[]> {
    return this.http.get<IStatie[]>(`${this.listaStatiiUrl}`);
  }
  //

  create(statie: IStatie): Observable<EntityResponseType> {
    return this.http.post<IStatie>(this.resourceUrl, statie, { observe: 'response' });
  }

  update(statie: IStatie): Observable<EntityResponseType> {
    return this.http.put<IStatie>(this.resourceUrl, statie, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStatie>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStatie[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
