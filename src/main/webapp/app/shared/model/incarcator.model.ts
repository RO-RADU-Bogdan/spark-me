import { IStatie } from 'app/shared/model/statie.model';
import { Conector } from 'app/shared/model/enumerations/conector.model';
import { Disponibilitate } from 'app/shared/model/enumerations/disponibilitate.model';

export interface IIncarcator {
  id?: number;
  denumireConector?: string;
  conector?: Conector;
  descriereConector?: string;
  disponibilitate?: Disponibilitate;
  putere?: number;
  statie?: IStatie;
}

export class Incarcator implements IIncarcator {
  constructor(
    public id?: number,
    public denumireConector?: string,
    public conector?: Conector,
    public descriereConector?: string,
    public disponibilitate?: Disponibilitate,
    public putere?: number,
    public statie?: IStatie
  ) {}
}
