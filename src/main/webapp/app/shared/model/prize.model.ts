import { IImage } from 'app/shared/model/image.model';

export interface IPrize {
  id?: number;
  name?: string;
  description?: string;
  images?: IImage[];
}

export class Prize implements IPrize {
  constructor(public id?: number, public name?: string, public description?: string, public images?: IImage[]) {}
}
