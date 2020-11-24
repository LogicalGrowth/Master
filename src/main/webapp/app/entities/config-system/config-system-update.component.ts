import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IConfigSystem, ConfigSystem } from 'app/shared/model/config-system.model';
import { ConfigSystemService } from './config-system.service';

@Component({
  selector: 'jhi-config-system-update',
  templateUrl: './config-system-update.component.html',
})
export class ConfigSystemUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    type: [null, [Validators.required]],
    value: [null, [Validators.required]],
  });

  constructor(protected configSystemService: ConfigSystemService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ configSystem }) => {
      this.updateForm(configSystem);
    });
  }

  updateForm(configSystem: IConfigSystem): void {
    this.editForm.patchValue({
      id: configSystem.id,
      type: configSystem.type,
      value: configSystem.value,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const configSystem = this.createFromForm();
    if (configSystem.id !== undefined) {
      this.subscribeToSaveResponse(this.configSystemService.update(configSystem));
    } else {
      this.subscribeToSaveResponse(this.configSystemService.create(configSystem));
    }
  }

  private createFromForm(): IConfigSystem {
    return {
      ...new ConfigSystem(),
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value,
      value: this.editForm.get(['value'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConfigSystem>>): void {
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
}
