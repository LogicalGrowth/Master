import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Fun4FundTestModule } from '../../../test.module';
import { RecommendationDetailComponent } from 'app/entities/recommendation/recommendation-detail.component';
import { Recommendation } from 'app/shared/model/recommendation.model';

describe('Component Tests', () => {
  describe('Recommendation Management Detail Component', () => {
    let comp: RecommendationDetailComponent;
    let fixture: ComponentFixture<RecommendationDetailComponent>;
    const route = ({ data: of({ recommendation: new Recommendation(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [RecommendationDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(RecommendationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RecommendationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load recommendation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.recommendation).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
