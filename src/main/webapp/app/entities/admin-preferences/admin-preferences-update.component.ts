import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAdminPreferences, AdminPreferences } from 'app/shared/model/admin-preferences.model';
import { AdminPreferencesService } from './admin-preferences.service';

@Component({
  selector: 'jhi-admin-preferences-update',
  templateUrl: './admin-preferences-update.component.html',
})
export class AdminPreferencesUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    inactivityTime: [null, [Validators.required]],
    notificationRecurrence: [null, [Validators.required]],
  });

  constructor(
    protected adminPreferencesService: AdminPreferencesService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ adminPreferences }) => {
      this.updateForm(adminPreferences);
    });
  }

  updateForm(adminPreferences: IAdminPreferences): void {
    this.editForm.patchValue({
      id: adminPreferences.id,
      inactivityTime: adminPreferences.inactivityTime,
      notificationRecurrence: adminPreferences.notificationRecurrence,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const adminPreferences = this.createFromForm();
    if (adminPreferences.id !== undefined) {
      this.subscribeToSaveResponse(this.adminPreferencesService.update(adminPreferences));
    } else {
      this.subscribeToSaveResponse(this.adminPreferencesService.create(adminPreferences));
    }
  }

  private createFromForm(): IAdminPreferences {
    return {
      ...new AdminPreferences(),
      id: this.editForm.get(['id'])!.value,
      inactivityTime: this.editForm.get(['inactivityTime'])!.value,
      notificationRecurrence: this.editForm.get(['notificationRecurrence'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAdminPreferences>>): void {
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
