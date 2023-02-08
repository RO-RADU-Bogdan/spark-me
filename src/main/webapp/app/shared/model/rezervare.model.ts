import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IIncarcator } from 'app/shared/model/incarcator.model';
import { StatutRezervare } from 'app/shared/model/enumerations/statut-rezervare.model';

export interface IRezervare {
  id?: number;
  dataCreare?: Moment;
  dataExpirare?: Moment;
  dataStart?: Moment;
  dataFinal?: Moment;
  statut?: StatutRezervare;
  user?: IUser;
  incarcator?: IIncarcator;
}

export class Rezervare implements IRezervare {
  constructor(
    public id?: number,
    public dataCreare?: Moment,
    public dataExpirare?: Moment,
    public dataStart?: Moment,
    public dataFinal?: Moment,
    public statut?: StatutRezervare,
    public user?: IUser,
    public incarcator?: IIncarcator
  ) {}
}
