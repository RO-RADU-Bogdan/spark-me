import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IRezervare } from 'app/shared/model/rezervare.model';
import { UserService } from 'app/core/user/user.service';
import { IUser } from 'app/core/user/user.model';

type EntityResponseType = HttpResponse<IRezervare>;
type EntityArrayResponseType = HttpResponse<IRezervare[]>;

@Injectable({ providedIn: 'root' })
export class RezervareService {
  public resourceUrl = SERVER_API_URL + 'api/rezervares';

  // Custom APIs URLs:
  public rezervareCurentaUrl = SERVER_API_URL + 'api/rezervareCurrentUser';
  public listaRezervariUrl = SERVER_API_URL + 'api/listaRezervari';
  public checkStatutRezervariUrl = SERVER_API_URL + 'api/checkAllRezervari';

  public startThreadCheckRezervariUrl = SERVER_API_URL + 'api/startThreadCheckRezervari';
  //

  //
  currentUser: IUser = null;
  rezervareCurrentUser: IRezervare = null;
  userAreRezervare = false;
  //

  constructor(protected http: HttpClient, protected userService: UserService) {}

  create(rezervare: IRezervare): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rezervare);
    return this.http
      .post<IRezervare>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(rezervare: IRezervare): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rezervare);
    return this.http
      .put<IRezervare>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  // Update pt. statut rezervare:
  updateS(rezervare: IRezervare): Observable<EntityResponseType> {
    return this.http.put<IRezervare>(this.resourceUrl, rezervare, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRezervare>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  // Start thread check rezervari:
  startThreadCheckRezervari(): Observable<any> {
    return this.http.get<IRezervare>(`${this.startThreadCheckRezervariUrl}`);
  }

  // Verifica statutul rezervarilor:
  checkStatutRezervari(): Observable<any> {
    return this.http.get<IRezervare>(`${this.checkStatutRezervariUrl}`);
  }
  //

  // Gaseste rezervarea user-ului logat curent:
  findCurrentRezervare(): Observable<IRezervare> {
    return this.http.get<IRezervare>(`${this.rezervareCurentaUrl}`);
  }

  // Gaseste rezervarile user-ului logat curent:
  findCurrentRezervari(): Observable<IRezervare[]> {
    return this.http.get<IRezervare[]>(`${this.rezervareCurentaUrl}`);
  }

  // Gaseste toate rezervarile existente:
  getListaRezervari(): Observable<IRezervare[]> {
    return this.http.get<IRezervare[]>(`${this.listaRezervariUrl}`);
  }

  // Gaseste toate rezervarile existente:
  getListRezervari(): IRezervare[] {
    let listaRezervari = null;
    listaRezervari = this.http.get<IRezervare[]>(`${this.listaRezervariUrl}`);

    return listaRezervari;
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRezervare[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(rezervare: IRezervare): IRezervare {
    const copy: IRezervare = Object.assign({}, rezervare, {
      dataCreare: rezervare.dataCreare && rezervare.dataCreare.isValid() ? rezervare.dataCreare.toJSON() : undefined,
      dataExpirare: rezervare.dataExpirare && rezervare.dataExpirare.isValid() ? rezervare.dataExpirare.toJSON() : undefined,
      dataStart: rezervare.dataStart && rezervare.dataStart.isValid() ? rezervare.dataStart.toJSON() : undefined,
      dataFinal: rezervare.dataFinal && rezervare.dataFinal.isValid() ? rezervare.dataFinal.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dataCreare = res.body.dataCreare ? moment(res.body.dataCreare) : undefined;
      res.body.dataExpirare = res.body.dataExpirare ? moment(res.body.dataExpirare) : undefined;
      res.body.dataStart = res.body.dataStart ? moment(res.body.dataStart) : undefined;
      res.body.dataFinal = res.body.dataFinal ? moment(res.body.dataFinal) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((rezervare: IRezervare) => {
        rezervare.dataCreare = rezervare.dataCreare ? moment(rezervare.dataCreare) : undefined;
        rezervare.dataExpirare = rezervare.dataExpirare ? moment(rezervare.dataExpirare) : undefined;
        rezervare.dataStart = rezervare.dataStart ? moment(rezervare.dataStart) : undefined;
        rezervare.dataFinal = rezervare.dataFinal ? moment(rezervare.dataFinal) : undefined;
      });
    }
    return res;
  }
}
