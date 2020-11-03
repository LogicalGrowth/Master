export interface IFee {
  id?: number;
  percentage?: number;
  highestAmount?: number;
}

export class Fee implements IFee {
  constructor(public id?: number, public percentage?: number, public highestAmount?: number) {}
}
