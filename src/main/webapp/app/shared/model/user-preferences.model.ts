export interface IUserPreferences {
  id?: number;
  notifications?: boolean;
}

export class UserPreferences implements IUserPreferences {
  constructor(public id?: number, public notifications?: boolean) {
    this.notifications = this.notifications || false;
  }
}
