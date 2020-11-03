import { Moment } from 'moment';
import { ActionType } from 'app/shared/model/enumerations/action-type.model';

export interface IAppLog {
  id?: number;
  timeStamp?: Moment;
  action?: ActionType;
  user?: string;
  description?: string;
}

export class AppLog implements IAppLog {
  constructor(
    public id?: number,
    public timeStamp?: Moment,
    public action?: ActionType,
    public user?: string,
    public description?: string
  ) {}
}
