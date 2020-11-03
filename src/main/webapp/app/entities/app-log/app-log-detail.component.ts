import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAppLog } from 'app/shared/model/app-log.model';

@Component({
  selector: 'jhi-app-log-detail',
  templateUrl: './app-log-detail.component.html',
})
export class AppLogDetailComponent implements OnInit {
  appLog: IAppLog | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appLog }) => (this.appLog = appLog));
  }

  previousState(): void {
    window.history.back();
  }
}
