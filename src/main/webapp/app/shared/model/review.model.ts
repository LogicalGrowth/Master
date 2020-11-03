import { Moment } from 'moment';
import { IProyect } from 'app/shared/model/proyect.model';

export interface IReview {
  id?: number;
  timeStamp?: Moment;
  message?: string;
  user?: string;
  rating?: number;
  proyect?: IProyect;
}

export class Review implements IReview {
  constructor(
    public id?: number,
    public timeStamp?: Moment,
    public message?: string,
    public user?: string,
    public rating?: number,
    public proyect?: IProyect
  ) {}
}
