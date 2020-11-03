import { Moment } from 'moment';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { IProyect } from 'app/shared/model/proyect.model';

export interface IDonationHistory {
  id?: number;
  amount?: number;
  timeStamp?: Moment;
  applicationUser?: IApplicationUser;
  proyect?: IProyect;
}

export class DonationHistory implements IDonationHistory {
  constructor(
    public id?: number,
    public amount?: number,
    public timeStamp?: Moment,
    public applicationUser?: IApplicationUser,
    public proyect?: IProyect
  ) {}
}
