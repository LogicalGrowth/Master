import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IPasswordHistory, PasswordHistory } from 'app/shared/model/password-history.model';
import { PasswordHistoryService } from './password-history.service';

@Component({
  selector: 'jhi-password-history-update',
  templateUrl: './password-history-update.component.html',
})
export class PasswordHistoryUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    password: [],
    startDate: [],
    endDate: [],
    status: [],
  });

  constructor(
    protected passwordHistoryService: PasswordHistoryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ passwordHistory }) => {
      if (!passwordHistory.id) {
        const today = moment().startOf('day');
        passwordHistory.startDate = today;
        passwordHistory.endDate = today;
      }

      this.updateForm(passwordHistory);
    });
  }

  updateForm(passwordHistory: IPasswordHistory): void {
    this.editForm.patchValue({
      id: passwordHistory.id,
      password: passwordHistory.password,
      startDate: passwordHistory.startDate ? passwordHistory.startDate.format(DATE_TIME_FORMAT) : null,
      endDate: passwordHistory.endDate ? passwordHistory.endDate.format(DATE_TIME_FORMAT) : null,
      status: passwordHistory.status,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const passwordHistory = this.createFromForm();
    if (passwordHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.passwordHistoryService.update(passwordHistory));
    } else {
      this.subscribeToSaveResponse(this.passwordHistoryService.create(passwordHistory));
    }
  }

  private createFromForm(): IPasswordHistory {
    return {
      ...new PasswordHistory(),
      id: this.editForm.get(['id'])!.value,
      password: this.editForm.get(['password'])!.value,
      startDate: this.editForm.get(['startDate'])!.value ? moment(this.editForm.get(['startDate'])!.value, DATE_TIME_FORMAT) : undefined,
      endDate: this.editForm.get(['endDate'])!.value ? moment(this.editForm.get(['endDate'])!.value, DATE_TIME_FORMAT) : undefined,
      status: this.editForm.get(['status'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPasswordHistory>>): void {
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
