import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import { IProyect } from 'app/shared/model/proyect.model';
import { INotification } from 'app/shared/model/notification.model';
import { IPayment } from 'app/shared/model/payment.model';
import { IAuction } from 'app/shared/model/auction.model';
import { IPartnerRequest } from 'app/shared/model/partner-request.model';
import { ITicket } from 'app/shared/model/ticket.model';
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
  notifications?: INotification[];
  payments?: IPayment[];
  auctions?: IAuction[];
  partnerRequests?: IPartnerRequest[];
  tickets?: ITicket[];
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
    public notifications?: INotification[],
    public payments?: IPayment[],
    public auctions?: IAuction[],
    public partnerRequests?: IPartnerRequest[],
    public tickets?: ITicket[],
    public favorites?: IProyect[]
  ) {
    this.admin = this.admin || false;
  }
}
