import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRecommendation } from 'app/shared/model/recommendation.model';

@Component({
  selector: 'jhi-recommendation-detail',
  templateUrl: './recommendation-detail.component.html',
})
export class RecommendationDetailComponent implements OnInit {
  recommendation: IRecommendation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recommendation }) => (this.recommendation = recommendation));
  }

  previousState(): void {
    window.history.back();
  }
}
