export interface IRecommendation {
  id?: number;
  description?: string;
}

export class Recommendation implements IRecommendation {
  constructor(public id?: number, public description?: string) {}
}
