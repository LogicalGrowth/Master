import { Moment } from 'moment';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { IProyect } from 'app/shared/model/proyect.model';
import { ProductType } from 'app/shared/model/enumerations/product-type.model';

export interface IPayment {
  id?: number;
  amount?: number;
  type?: ProductType;
  timeStamp?: Moment;
  applicationUser?: IApplicationUser;
  proyect?: IProyect;
}

export class Payment implements IPayment {
  constructor(
    public id?: number,
    public amount?: number,
    public type?: ProductType,
    public timeStamp?: Moment,
    public applicationUser?: IApplicationUser,
    public proyect?: IProyect
  ) {}
}
