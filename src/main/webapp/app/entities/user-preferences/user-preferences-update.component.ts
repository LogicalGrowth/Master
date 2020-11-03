import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IUserPreferences, UserPreferences } from 'app/shared/model/user-preferences.model';
import { UserPreferencesService } from './user-preferences.service';

@Component({
  selector: 'jhi-user-preferences-update',
  templateUrl: './user-preferences-update.component.html',
})
export class UserPreferencesUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    notifications: [null, [Validators.required]],
  });

  constructor(
    protected userPreferencesService: UserPreferencesService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userPreferences }) => {
      this.updateForm(userPreferences);
    });
  }

  updateForm(userPreferences: IUserPreferences): void {
    this.editForm.patchValue({
      id: userPreferences.id,
      notifications: userPreferences.notifications,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userPreferences = this.createFromForm();
    if (userPreferences.id !== undefined) {
      this.subscribeToSaveResponse(this.userPreferencesService.update(userPreferences));
    } else {
      this.subscribeToSaveResponse(this.userPreferencesService.create(userPreferences));
    }
  }

  private createFromForm(): IUserPreferences {
    return {
      ...new UserPreferences(),
      id: this.editForm.get(['id'])!.value,
      notifications: this.editForm.get(['notifications'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserPreferences>>): void {
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
