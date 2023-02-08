import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IIncarcator } from 'app/shared/model/incarcator.model';
import { Disponibilitate } from 'app/shared/model/enumerations/disponibilitate.model';

type EntityResponseType = HttpResponse<IIncarcator>;
type EntityArrayResponseType = HttpResponse<IIncarcator[]>;

@Injectable({ providedIn: 'root' })
export class IncarcatorService {
  public resourceUrl = SERVER_API_URL + 'api/incarcators';

  //
  incarcatorRezervat: IIncarcator = null;
  //

  constructor(protected http: HttpClient) {}

  // Rezervare incarcator (doar front-end, daca trece de Save din rezervare se duce si in DB):
  rezervaIncarcator(incarcator: IIncarcator): void {
    this.incarcatorRezervat = incarcator;
  }

  // Update incarcator eliberat:
  updateEliberat(): Observable<EntityResponseType> {
    this.incarcatorRezervat.disponibilitate = Disponibilitate.DISPONIBIL; // elibereaza incarcator
    return this.http.put<IIncarcator>(this.resourceUrl, this.incarcatorRezervat, { observe: 'response' });
  }
  //

  // Intoarce incarcator rezervat:
  getIncarcatorRezervat(): IIncarcator {
    return this.incarcatorRezervat;
  }
  //

  // Trebuie intors incarcatorul rezervat de user din DB:
  findRezervat(id: number): Observable<IIncarcator> {
    return this.http.get<IIncarcator>(`${this.resourceUrl}/${id}`);
  }

  create(incarcator: IIncarcator): Observable<EntityResponseType> {
    return this.http.post<IIncarcator>(this.resourceUrl, incarcator, { observe: 'response' });
  }

  update(incarcator: IIncarcator): Observable<EntityResponseType> {
    return this.http.put<IIncarcator>(this.resourceUrl, incarcator, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIncarcator>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIncarcator[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
