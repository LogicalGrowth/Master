import { Moment } from 'moment';
import { IPrize } from 'app/shared/model/prize.model';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { IProyect } from 'app/shared/model/proyect.model';
import { ActivityStatus } from 'app/shared/model/enumerations/activity-status.model';

export interface IRaffle {
  id?: number;
  price?: number;
  totalTicket?: number;
  expirationDate?: Moment;
  state?: ActivityStatus;
  prize?: IPrize;
  buyer?: IApplicationUser;
  proyect?: IProyect;
}

export class Raffle implements IRaffle {
  constructor(
    public id?: number,
    public price?: number,
    public totalTicket?: number,
    public expirationDate?: Moment,
    public state?: ActivityStatus,
    public prize?: IPrize,
    public buyer?: IApplicationUser,
    public proyect?: IProyect
  ) {}
}
