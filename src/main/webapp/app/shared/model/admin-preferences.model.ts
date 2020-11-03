export interface IAdminPreferences {
  id?: number;
  inactivityTime?: number;
  notificationRecurrence?: number;
}

export class AdminPreferences implements IAdminPreferences {
  constructor(public id?: number, public inactivityTime?: number, public notificationRecurrence?: number) {}
}
