import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { INotification, Notification } from 'app/shared/model/notification.model';
import { NotificationService } from './notification.service';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/application-user.service';

@Component({
  selector: 'jhi-notification-update',
  templateUrl: './notification-update.component.html',
})
export class NotificationUpdateComponent implements OnInit {
  isSaving = false;
  applicationusers: IApplicationUser[] = [];

  editForm = this.fb.group({
    id: [],
    type: [null, [Validators.required]],
    message: [null, [Validators.required]],
    timeStamp: [null, [Validators.required]],
    status: [],
    applicationUser: [],
  });

  constructor(
    protected notificationService: NotificationService,
    protected applicationUserService: ApplicationUserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notification }) => {
      if (!notification.id) {
        const today = moment().startOf('day');
        notification.timeStamp = today;
      }

      this.updateForm(notification);

      this.applicationUserService.query().subscribe((res: HttpResponse<IApplicationUser[]>) => (this.applicationusers = res.body || []));
    });
  }

  updateForm(notification: INotification): void {
    this.editForm.patchValue({
      id: notification.id,
      type: notification.type,
      message: notification.message,
      timeStamp: notification.timeStamp ? notification.timeStamp.format(DATE_TIME_FORMAT) : null,
      status: notification.status,
      applicationUser: notification.applicationUser,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const notification = this.createFromForm();
    if (notification.id !== undefined) {
      this.subscribeToSaveResponse(this.notificationService.update(notification));
    } else {
      this.subscribeToSaveResponse(this.notificationService.create(notification));
    }
  }

  private createFromForm(): INotification {
    return {
      ...new Notification(),
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value,
      message: this.editForm.get(['message'])!.value,
      timeStamp: this.editForm.get(['timeStamp'])!.value ? moment(this.editForm.get(['timeStamp'])!.value, DATE_TIME_FORMAT) : undefined,
      status: this.editForm.get(['status'])!.value,
      applicationUser: this.editForm.get(['applicationUser'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INotification>>): void {
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

  trackById(index: number, item: IApplicationUser): any {
    return item.id;
  }
}
