import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConfigSystem } from 'app/shared/model/config-system.model';

@Component({
  selector: 'jhi-config-system-detail',
  templateUrl: './config-system-detail.component.html',
})
export class ConfigSystemDetailComponent implements OnInit {
  configSystem: IConfigSystem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ configSystem }) => (this.configSystem = configSystem));
  }

  previousState(): void {
    window.history.back();
  }
}
