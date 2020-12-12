import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { IProyect } from 'app/shared/model/proyect.model';
import { ProyectService } from '../proyect.service';

@Component({
  selector: 'jhi-proyect-management',
  templateUrl: './proyect-management.component.html',
  styleUrls: ['../../../../content/scss/paper-dashboard.scss'],
})
export class ProyectManagementComponent implements OnInit {
  proyects?: IProyect[];

  constructor(protected proyectService: ProyectService) {}

  loadAll(): void {
    this.proyectService.query().subscribe((res: HttpResponse<IProyect[]>) => (this.proyects = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IProyect): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  setActive(proyect: IProyect, isActivated: boolean): void {
    proyect.status = isActivated;
    this.proyectService.update({ ...proyect }).subscribe(() => this.loadAll());
  }
}
