import { IUser } from 'app/core/user/user.model';
import { TipAcces } from 'app/shared/model/enumerations/tip-acces.model';

export interface ILocatie {
  id?: number;
  denumire?: string;
  descriere?: string;
  latitudine?: number;
  longitudine?: number;
  tipAcces?: TipAcces;
  user?: IUser;
}

export class Locatie implements ILocatie {
  constructor(
    public id?: number,
    public denumire?: string,
    public descriere?: string,
    public latitudine?: number,
    public longitudine?: number,
    public tipAcces?: TipAcces,
    public user?: IUser
  ) {}
}
