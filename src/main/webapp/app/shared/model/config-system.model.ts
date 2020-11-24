export interface IConfigSystem {
  id?: number;
  type?: string;
  value?: string;
}

export class ConfigSystem implements IConfigSystem {
  constructor(public id?: number, public type?: string, public value?: string) {}
}
