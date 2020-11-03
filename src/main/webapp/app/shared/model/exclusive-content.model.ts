import { IPrize } from 'app/shared/model/prize.model';
import { IProyect } from 'app/shared/model/proyect.model';
import { ActivityStatus } from 'app/shared/model/enumerations/activity-status.model';

export interface IExclusiveContent {
  id?: number;
  price?: number;
  stock?: number;
  state?: ActivityStatus;
  prize?: IPrize;
  proyect?: IProyect;
}

export class ExclusiveContent implements IExclusiveContent {
  constructor(
    public id?: number,
    public price?: number,
    public stock?: number,
    public state?: ActivityStatus,
    public prize?: IPrize,
    public proyect?: IProyect
  ) {}
}
