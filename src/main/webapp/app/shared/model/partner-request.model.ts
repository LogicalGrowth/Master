import { IApplicationUser } from 'app/shared/model/application-user.model';
import { IProyect } from 'app/shared/model/proyect.model';
import { RequestStatus } from 'app/shared/model/enumerations/request-status.model';

export interface IPartnerRequest {
  id?: number;
  amount?: number;
  status?: RequestStatus;
  applicant?: IApplicationUser;
  proyect?: IProyect;
}

export class PartnerRequest implements IPartnerRequest {
  constructor(
    public id?: number,
    public amount?: number,
    public status?: RequestStatus,
    public applicant?: IApplicationUser,
    public proyect?: IProyect
  ) {}
}
