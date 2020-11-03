import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRecommendation } from 'app/shared/model/recommendation.model';
import { RecommendationService } from './recommendation.service';

@Component({
  templateUrl: './recommendation-delete-dialog.component.html',
})
export class RecommendationDeleteDialogComponent {
  recommendation?: IRecommendation;

  constructor(
    protected recommendationService: RecommendationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.recommendationService.delete(id).subscribe(() => {
      this.eventManager.broadcast('recommendationListModification');
      this.activeModal.close();
    });
  }
}
