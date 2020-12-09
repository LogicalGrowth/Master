import { IApplicationUser } from 'app/shared/model/application-user.model';
import { IProyect } from 'app/shared/model/proyect.model';

export interface IFavorite {
  id?: number;
  user?: IApplicationUser;
  proyect?: IProyect;
}

export class Favorite implements IFavorite {
  constructor(public id?: number, public user?: IApplicationUser, public proyect?: IProyect) {}
}
