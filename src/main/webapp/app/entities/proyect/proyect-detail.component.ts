import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IExclusiveContent } from 'app/shared/model/exclusive-content.model';

import { IProyect } from 'app/shared/model/proyect.model';
import { ExclusiveContentService } from '../exclusive-content/exclusive-content.service';

@Component({
  selector: 'jhi-proyect-detail',
  templateUrl: './proyect-detail.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class ProyectDetailComponent implements OnInit {
  proyect: IProyect | null = null;
  exclusiveContents?: IExclusiveContent[];

  constructor(protected activatedRoute: ActivatedRoute, protected exclusiveContentService: ExclusiveContentService) {}

  loadExclusiveContent(projectId: number): void {
    if (this.proyect != null) {
      this.exclusiveContentService
        .findAllByProject(projectId)
        .subscribe((res: HttpResponse<IExclusiveContent[]>) => (this.exclusiveContents = res.body || []));
    }
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proyect }) => (this.proyect = proyect));
    this.loadExclusiveContent(this.proyect?.id as number);
  }

  previousState(): void {
    window.history.back();
  }
}
