import { Moment } from 'moment';
import { IResource } from 'app/shared/model/resource.model';
import { ICheckpoint } from 'app/shared/model/checkpoint.model';
import { IReview } from 'app/shared/model/review.model';
import { IPartnerRequest } from 'app/shared/model/partner-request.model';
import { IRaffle } from 'app/shared/model/raffle.model';
import { IAuction } from 'app/shared/model/auction.model';
import { IExclusiveContent } from 'app/shared/model/exclusive-content.model';
import { IPayment } from 'app/shared/model/payment.model';
import { IFavorite } from 'app/shared/model/favorite.model';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { ICategory } from 'app/shared/model/category.model';
import { ProyectType } from 'app/shared/model/enumerations/proyect-type.model';
import { Currency } from 'app/shared/model/enumerations/currency.model';

export interface IProyect {
  id?: number;
  name?: string;
  description?: string;
  idType?: ProyectType;
  goalAmount?: number;
  collected?: number;
  rating?: number;
  creationDate?: Moment;
  lastUpdated?: Moment;
  coordX?: number;
  coordY?: number;
  fee?: number;
  number?: string;
  currencyType?: Currency;
  status?: boolean;
  images?: IResource[];
  checkpoints?: ICheckpoint[];
  reviews?: IReview[];
  partners?: IPartnerRequest[];
  raffles?: IRaffle[];
  auctions?: IAuction[];
  exclusiveContents?: IExclusiveContent[];
  payments?: IPayment[];
  favorites?: IFavorite[];
  owner?: IApplicationUser;
  category?: ICategory;
  favorite?: boolean;
}

export class Proyect implements IProyect {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public idType?: ProyectType,
    public goalAmount?: number,
    public collected?: number,
    public rating?: number,
    public creationDate?: Moment,
    public lastUpdated?: Moment,
    public coordX?: number,
    public coordY?: number,
    public fee?: number,
    public number?: string,
    public currencyType?: Currency,
    public status?: boolean,
    public images?: IResource[],
    public checkpoints?: ICheckpoint[],
    public reviews?: IReview[],
    public partners?: IPartnerRequest[],
    public raffles?: IRaffle[],
    public auctions?: IAuction[],
    public exclusiveContents?: IExclusiveContent[],
    public payments?: IPayment[],
    public favorites?: IFavorite[],
    public owner?: IApplicationUser,
    public category?: ICategory,
    public favorite?: boolean
  ) {
    this.status = this.status || false;
    this.favorite = false;
  }
}
