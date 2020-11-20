import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICheckpoint, Checkpoint } from 'app/shared/model/checkpoint.model';
import { CheckpointService } from './checkpoint.service';
import { IProyect } from 'app/shared/model/proyect.model';
import { ProyectService } from 'app/entities/proyect/proyect.service';

@Component({
  selector: 'jhi-checkpoint-update',
  templateUrl: './checkpoint-update.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class CheckpointUpdateComponent implements OnInit {
  isSaving = false;
  proyects: IProyect[] = [];

  editForm = this.fb.group({
    id: [],
    completitionPercentage: [null, [Validators.required]],
    message: [null, [Validators.required]],
    completed: [],
    proyect: [],
  });

  constructor(
    protected checkpointService: CheckpointService,
    protected proyectService: ProyectService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ checkpoint }) => {
      if (checkpoint) {
        this.updateForm(checkpoint);

        this.proyectService.query().subscribe((res: HttpResponse<IProyect[]>) => (this.proyects = res.body || []));
      }
    });

    this.activatedRoute.data.subscribe(({ currentProyect }) => {
      if (currentProyect) {
        this.editForm.patchValue({
          id: undefined,
          proyect: currentProyect,
        });
      }
    });
  }

  updateForm(checkpoint: ICheckpoint): void {
    this.editForm.patchValue({
      id: checkpoint.id,
      completitionPercentage: checkpoint.completitionPercentage,
      message: checkpoint.message,
      completed: checkpoint.completed,
      proyect: checkpoint.proyect,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const checkpoint = this.createFromForm();
    if (checkpoint.id !== undefined) {
      this.subscribeToSaveResponse(this.checkpointService.update(checkpoint));
    } else {
      this.subscribeToSaveResponse(this.checkpointService.create(checkpoint));
    }
  }

  private createFromForm(): ICheckpoint {
    return {
      ...new Checkpoint(),
      id: this.editForm.get(['id'])!.value,
      completitionPercentage: this.editForm.get(['completitionPercentage'])!.value,
      message: this.editForm.get(['message'])!.value,
      completed: false,
      proyect: this.editForm.get(['proyect'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICheckpoint>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IProyect): any {
    return item.id;
  }
}
