import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRecommendation } from 'app/shared/model/recommendation.model';
import { RecommendationService } from './recommendation.service';
import { RecommendationDeleteDialogComponent } from './recommendation-delete-dialog.component';

@Component({
  selector: 'jhi-recommendation',
  templateUrl: './recommendation.component.html',
})
export class RecommendationComponent implements OnInit, OnDestroy {
  recommendations?: IRecommendation[];
  eventSubscriber?: Subscription;

  constructor(
    protected recommendationService: RecommendationService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.recommendationService.query().subscribe((res: HttpResponse<IRecommendation[]>) => (this.recommendations = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInRecommendations();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IRecommendation): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInRecommendations(): void {
    this.eventSubscriber = this.eventManager.subscribe('recommendationListModification', () => this.loadAll());
  }

  delete(recommendation: IRecommendation): void {
    const modalRef = this.modalService.open(RecommendationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.recommendation = recommendation;
  }
}
