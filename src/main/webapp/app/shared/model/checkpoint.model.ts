import { IProyect } from 'app/shared/model/proyect.model';

export interface ICheckpoint {
  id?: number;
  completitionPercentage?: number;
  message?: string;
  completed?: boolean;
  proyect?: IProyect;
}

export class Checkpoint implements ICheckpoint {
  constructor(
    public id?: number,
    public completitionPercentage?: number,
    public message?: string,
    public completed?: boolean,
    public proyect?: IProyect
  ) {
    this.completed = this.completed || false;
  }
}
