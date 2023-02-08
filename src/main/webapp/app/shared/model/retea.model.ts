export interface IRetea {
  id?: number;
  denumire?: string;
  descriere?: string;
}

export class Retea implements IRetea {
  constructor(public id?: number, public denumire?: string, public descriere?: string) {}
}
