import { Moment } from 'moment';
import { PasswordStatus } from 'app/shared/model/enumerations/password-status.model';

export interface IPasswordHistory {
  id?: number;
  password?: string;
  startDate?: Moment;
  endDate?: Moment;
  status?: PasswordStatus;
}

export class PasswordHistory implements IPasswordHistory {
  constructor(
    public id?: number,
    public password?: string,
    public startDate?: Moment,
    public endDate?: Moment,
    public status?: PasswordStatus
  ) {}
}
