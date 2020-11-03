import { Moment } from 'moment';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { NotificationType } from 'app/shared/model/enumerations/notification-type.model';
import { NotificationStatus } from 'app/shared/model/enumerations/notification-status.model';

export interface INotification {
  id?: number;
  type?: NotificationType;
  message?: string;
  timeStamp?: Moment;
  status?: NotificationStatus;
  applicationUser?: IApplicationUser;
}

export class Notification implements INotification {
  constructor(
    public id?: number,
    public type?: NotificationType,
    public message?: string,
    public timeStamp?: Moment,
    public status?: NotificationStatus,
    public applicationUser?: IApplicationUser
  ) {}
}
