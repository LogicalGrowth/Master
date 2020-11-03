import { Moment } from 'moment';
import { IPrize } from 'app/shared/model/prize.model';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { IProyect } from 'app/shared/model/proyect.model';
import { ActivityStatus } from 'app/shared/model/enumerations/activity-status.model';

export interface IAuction {
  id?: number;
  initialBid?: number;
  winningBid?: number;
  expirationDate?: Moment;
  state?: ActivityStatus;
  prize?: IPrize;
  winner?: IApplicationUser;
  proyect?: IProyect;
}

export class Auction implements IAuction {
  constructor(
    public id?: number,
    public initialBid?: number,
    public winningBid?: number,
    public expirationDate?: Moment,
    public state?: ActivityStatus,
    public prize?: IPrize,
    public winner?: IApplicationUser,
    public proyect?: IProyect
  ) {}
}
