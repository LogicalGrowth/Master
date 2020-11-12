import { IProyect } from 'app/shared/model/proyect.model';
import { IPrize } from 'app/shared/model/prize.model';

export interface IResource {
  id?: number;
  url?: string;
  type?: string;
  proyect?: IProyect;
  prize?: IPrize;
}

export class Resource implements IResource {
  constructor(public id?: number, public url?: string, public type?: string, public proyect?: IProyect, public prize?: IPrize) {}
}
