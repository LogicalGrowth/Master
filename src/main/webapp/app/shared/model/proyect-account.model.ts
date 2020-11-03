import { Currency } from 'app/shared/model/enumerations/currency.model';

export interface IProyectAccount {
  id?: number;
  number?: string;
  currencyType?: Currency;
}

export class ProyectAccount implements IProyectAccount {
  constructor(public id?: number, public number?: string, public currencyType?: Currency) {}
}
