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
  owner?: IApplicationUser;
}

export class PaymentMethod implements IPaymentMethod {
  constructor(
    public id?: number,
    public cardNumber?: string,
    public cardOwner?: string,
    public expirationDate?: Moment,
    public type?: CardType,
    public cvc?: string,
    public owner?: IApplicationUser
  ) {}
}
