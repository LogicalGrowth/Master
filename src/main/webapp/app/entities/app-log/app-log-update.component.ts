import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IAppLog, AppLog } from 'app/shared/model/app-log.model';
import { AppLogService } from './app-log.service';

@Component({
  selector: 'jhi-app-log-update',
  templateUrl: './app-log-update.component.html',
})
export class AppLogUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    timeStamp: [null, [Validators.required]],
    action: [null, [Validators.required]],
    user: [null, [Validators.required]],
    description: [null, [Validators.required]],
  });

  constructor(protected appLogService: AppLogService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appLog }) => {
      if (!appLog.id) {
        const today = moment().startOf('day');
        appLog.timeStamp = today;
      }

      this.updateForm(appLog);
    });
  }

  updateForm(appLog: IAppLog): void {
    this.editForm.patchValue({
      id: appLog.id,
      timeStamp: appLog.timeStamp ? appLog.timeStamp.format(DATE_TIME_FORMAT) : null,
      action: appLog.action,
      user: appLog.user,
      description: appLog.description,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appLog = this.createFromForm();
    if (appLog.id !== undefined) {
      this.subscribeToSaveResponse(this.appLogService.update(appLog));
    } else {
      this.subscribeToSaveResponse(this.appLogService.create(appLog));
    }
  }

  private createFromForm(): IAppLog {
    return {
      ...new AppLog(),
      id: this.editForm.get(['id'])!.value,
      timeStamp: this.editForm.get(['timeStamp'])!.value ? moment(this.editForm.get(['timeStamp'])!.value, DATE_TIME_FORMAT) : undefined,
      action: this.editForm.get(['action'])!.value,
      user: this.editForm.get(['user'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppLog>>): void {
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
