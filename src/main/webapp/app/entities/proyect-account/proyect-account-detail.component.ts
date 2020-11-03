import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProyectAccount } from 'app/shared/model/proyect-account.model';

@Component({
  selector: 'jhi-proyect-account-detail',
  templateUrl: './proyect-account-detail.component.html',
})
export class ProyectAccountDetailComponent implements OnInit {
  proyectAccount: IProyectAccount | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proyectAccount }) => (this.proyectAccount = proyectAccount));
  }

  previousState(): void {
    window.history.back();
  }
}
