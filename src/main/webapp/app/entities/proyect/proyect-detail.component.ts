import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProyect } from 'app/shared/model/proyect.model';

@Component({
  selector: 'jhi-proyect-detail',
  templateUrl: './proyect-detail.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss', '../../../content/scss/paper-dashboard/rating/rating.scss'],
})
export class ProyectDetailComponent implements OnInit {
  proyect: IProyect | null = null;
  ratings = [1, 2, 3, 4, 5];

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proyect }) => (this.proyect = proyect));
  }
}
