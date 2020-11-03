import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IReview, Review } from 'app/shared/model/review.model';
import { ReviewService } from './review.service';
import { IProyect } from 'app/shared/model/proyect.model';
import { ProyectService } from 'app/entities/proyect/proyect.service';

@Component({
  selector: 'jhi-review-update',
  templateUrl: './review-update.component.html',
})
export class ReviewUpdateComponent implements OnInit {
  isSaving = false;
  proyects: IProyect[] = [];

  editForm = this.fb.group({
    id: [],
    timeStamp: [null, [Validators.required]],
    message: [null, [Validators.required]],
    user: [null, [Validators.required]],
    rating: [null, [Validators.required]],
    proyect: [],
  });

  constructor(
    protected reviewService: ReviewService,
    protected proyectService: ProyectService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ review }) => {
      if (!review.id) {
        const today = moment().startOf('day');
        review.timeStamp = today;
      }

      this.updateForm(review);

      this.proyectService.query().subscribe((res: HttpResponse<IProyect[]>) => (this.proyects = res.body || []));
    });
  }

  updateForm(review: IReview): void {
    this.editForm.patchValue({
      id: review.id,
      timeStamp: review.timeStamp ? review.timeStamp.format(DATE_TIME_FORMAT) : null,
      message: review.message,
      user: review.user,
      rating: review.rating,
      proyect: review.proyect,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const review = this.createFromForm();
    if (review.id !== undefined) {
      this.subscribeToSaveResponse(this.reviewService.update(review));
    } else {
      this.subscribeToSaveResponse(this.reviewService.create(review));
    }
  }

  private createFromForm(): IReview {
    return {
      ...new Review(),
      id: this.editForm.get(['id'])!.value,
      timeStamp: this.editForm.get(['timeStamp'])!.value ? moment(this.editForm.get(['timeStamp'])!.value, DATE_TIME_FORMAT) : undefined,
      message: this.editForm.get(['message'])!.value,
      user: this.editForm.get(['user'])!.value,
      rating: this.editForm.get(['rating'])!.value,
      proyect: this.editForm.get(['proyect'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReview>>): void {
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
