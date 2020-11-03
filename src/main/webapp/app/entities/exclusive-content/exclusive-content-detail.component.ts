import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExclusiveContent } from 'app/shared/model/exclusive-content.model';

@Component({
  selector: 'jhi-exclusive-content-detail',
  templateUrl: './exclusive-content-detail.component.html',
})
export class ExclusiveContentDetailComponent implements OnInit {
  exclusiveContent: IExclusiveContent | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exclusiveContent }) => (this.exclusiveContent = exclusiveContent));
  }

  previousState(): void {
    window.history.back();
  }
}
