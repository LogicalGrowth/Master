import { IProyect } from 'app/shared/model/proyect.model';
import { IPrize } from 'app/shared/model/prize.model';

export interface IImage {
  id?: number;
  url?: string;
  proyect?: IProyect;
  prize?: IPrize;
}

export class Image implements IImage {
  constructor(public id?: number, public url?: string, public proyect?: IProyect, public prize?: IPrize) {}
}
