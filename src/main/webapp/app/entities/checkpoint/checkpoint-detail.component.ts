import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICheckpoint } from 'app/shared/model/checkpoint.model';

@Component({
  selector: 'jhi-checkpoint-detail',
  templateUrl: './checkpoint-detail.component.html',
})
export class CheckpointDetailComponent implements OnInit {
  checkpoint: ICheckpoint | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ checkpoint }) => (this.checkpoint = checkpoint));
  }

  previousState(): void {
    window.history.back();
  }
}
