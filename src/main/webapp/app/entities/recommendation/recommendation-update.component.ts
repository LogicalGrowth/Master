import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IRecommendation, Recommendation } from 'app/shared/model/recommendation.model';
import { RecommendationService } from './recommendation.service';

@Component({
  selector: 'jhi-recommendation-update',
  templateUrl: './recommendation-update.component.html',
})
export class RecommendationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    description: [null, [Validators.required]],
  });

  constructor(protected recommendationService: RecommendationService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recommendation }) => {
      this.updateForm(recommendation);
    });
  }

  updateForm(recommendation: IRecommendation): void {
    this.editForm.patchValue({
      id: recommendation.id,
      description: recommendation.description,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const recommendation = this.createFromForm();
    if (recommendation.id !== undefined) {
      this.subscribeToSaveResponse(this.recommendationService.update(recommendation));
    } else {
      this.subscribeToSaveResponse(this.recommendationService.create(recommendation));
    }
  }

  private createFromForm(): IRecommendation {
    return {
      ...new Recommendation(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRecommendation>>): void {
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
