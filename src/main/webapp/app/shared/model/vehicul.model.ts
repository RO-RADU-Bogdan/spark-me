import { IUser } from 'app/core/user/user.model';
import { Conector } from 'app/shared/model/enumerations/conector.model';

export interface IVehicul {
  id?: number;
  marca?: string;
  model?: string;
  anFabricatie?: number;
  culoare?: string;
  descriere?: string;
  conector?: Conector;
  user?: IUser;
}

export class Vehicul implements IVehicul {
  constructor(
    public id?: number,
    public marca?: string,
    public model?: string,
    public anFabricatie?: number,
    public culoare?: string,
    public descriere?: string,
    public conector?: Conector,
    public user?: IUser
  ) {}
}
