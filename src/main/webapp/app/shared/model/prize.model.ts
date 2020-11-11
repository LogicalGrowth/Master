import { IResource } from 'app/shared/model/resource.model';

export interface IPrize {
  id?: number;
  name?: string;
  description?: string;
  images?: IResource[];
}

export class Prize implements IPrize {
  constructor(public id?: number, public name?: string, public description?: string, public images?: IResource[]) {}
}
