import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import { IProyect } from 'app/shared/model/proyect.model';
import { IDonationHistory } from 'app/shared/model/donation-history.model';
import { INotification } from 'app/shared/model/notification.model';
import { IPayment } from 'app/shared/model/payment.model';
import { IdType } from 'app/shared/model/enumerations/id-type.model';

export interface IApplicationUser {
  id?: number;
  identification?: string;
  idType?: IdType;
  birthDate?: Moment;
  phoneNumber?: string;
  admin?: boolean;
  internalUser?: IUser;
  paymentMethods?: IPaymentMethod[];
  proyects?: IProyect[];
  donations?: IDonationHistory[];
  notifications?: INotification[];
  payments?: IPayment[];
  favorites?: IProyect[];
}

export class ApplicationUser implements IApplicationUser {
  constructor(
    public id?: number,
    public identification?: string,
    public idType?: IdType,
    public birthDate?: Moment,
    public phoneNumber?: string,
    public admin?: boolean,
    public internalUser?: IUser,
    public paymentMethods?: IPaymentMethod[],
    public proyects?: IProyect[],
    public donations?: IDonationHistory[],
    public notifications?: INotification[],
    public payments?: IPayment[],
    public favorites?: IProyect[]
  ) {
    this.admin = this.admin || false;
  }
}
