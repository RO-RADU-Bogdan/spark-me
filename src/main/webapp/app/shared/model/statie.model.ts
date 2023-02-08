import { ILocatie } from 'app/shared/model/locatie.model';
import { IRetea } from 'app/shared/model/retea.model';
import { StatutStatie } from 'app/shared/model/enumerations/statut-statie.model';
import { TipCost } from 'app/shared/model/enumerations/tip-cost.model';

export interface IStatie {
  id?: number;
  denumire?: string;
  producator?: string;
  model?: string;
  latitudine?: number;
  longitudine?: number;
  statut?: StatutStatie;
  tipCost?: TipCost;
  descriereCost?: string;
  locatie?: ILocatie;
  retea?: IRetea;
}

export class Statie implements IStatie {
  constructor(
    public id?: number,
    public denumire?: string,
    public producator?: string,
    public model?: string,
    public latitudine?: number,
    public longitudine?: number,
    public statut?: StatutStatie,
    public tipCost?: TipCost,
    public descriereCost?: string,
    public locatie?: ILocatie,
    public retea?: IRetea
  ) {}
}
