import { IApplicationUser } from 'app/shared/model/application-user.model';
import { IRaffle } from 'app/shared/model/raffle.model';

export interface ITicket {
  id?: number;
  buyer?: IApplicationUser;
  raffle?: IRaffle;
}

export class Ticket implements ITicket {
  constructor(public id?: number, public buyer?: IApplicationUser, public raffle?: IRaffle) {}
}
