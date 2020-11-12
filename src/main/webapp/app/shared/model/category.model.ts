import { IResource } from 'app/shared/model/resource.model';
import { IProyect } from 'app/shared/model/proyect.model';
import { CategoryStatus } from 'app/shared/model/enumerations/category-status.model';

export interface ICategory {
  id?: number;
  name?: string;
  description?: string;
  status?: CategoryStatus;
  image?: IResource;
  proyects?: IProyect[];
}

export class Category implements ICategory {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public status?: CategoryStatus,
    public image?: IResource,
    public proyects?: IProyect[]
  ) {}
}
