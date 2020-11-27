import { Moment } from 'moment';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { CardType } from 'app/shared/model/enumerations/card-type.model';

export interface IPaymentMethod {
  id?: number;
  cardNumber?: string;
  cardOwner?: string;
  expirationDate?: Moment;
  type?: CardType;
  cvc?: string;
  favorite?: boolean;
  owner?: IApplicationUser;
  typeImage?: string;
}

export class PaymentMethod implements IPaymentMethod {
  constructor(
    public id?: number,
    public cardNumber?: string,
    public cardOwner?: string,
    public expirationDate?: Moment,
    public type?: CardType,
    public cvc?: string,
    public typeImage?: string,
    public favorite?: boolean,
    public owner?: IApplicationUser
  ) {
    this.favorite = this.favorite || false;
  }
}
