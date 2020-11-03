import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Fun4FundTestModule } from '../../../test.module';
import { RecommendationComponent } from 'app/entities/recommendation/recommendation.component';
import { RecommendationService } from 'app/entities/recommendation/recommendation.service';
import { Recommendation } from 'app/shared/model/recommendation.model';

describe('Component Tests', () => {
  describe('Recommendation Management Component', () => {
    let comp: RecommendationComponent;
    let fixture: ComponentFixture<RecommendationComponent>;
    let service: RecommendationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [RecommendationComponent],
      })
        .overrideTemplate(RecommendationComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RecommendationComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RecommendationService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Recommendation(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.recommendations && comp.recommendations[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
