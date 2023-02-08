import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IVehicul } from 'app/shared/model/vehicul.model';

type EntityResponseType = HttpResponse<IVehicul>;
type EntityArrayResponseType = HttpResponse<IVehicul[]>;

@Injectable({ providedIn: 'root' })
export class VehiculService {
  public resourceUrl = SERVER_API_URL + 'api/vehiculs';

  constructor(protected http: HttpClient) {}

  create(vehicul: IVehicul): Observable<EntityResponseType> {
    return this.http.post<IVehicul>(this.resourceUrl, vehicul, { observe: 'response' });
  }

  update(vehicul: IVehicul): Observable<EntityResponseType> {
    return this.http.put<IVehicul>(this.resourceUrl, vehicul, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVehicul>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVehicul[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
