import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Fun4FundTestModule } from '../../../test.module';
import { RecommendationUpdateComponent } from 'app/entities/recommendation/recommendation-update.component';
import { RecommendationService } from 'app/entities/recommendation/recommendation.service';
import { Recommendation } from 'app/shared/model/recommendation.model';

describe('Component Tests', () => {
  describe('Recommendation Management Update Component', () => {
    let comp: RecommendationUpdateComponent;
    let fixture: ComponentFixture<RecommendationUpdateComponent>;
    let service: RecommendationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [RecommendationUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(RecommendationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RecommendationUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RecommendationService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Recommendation(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Recommendation();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
