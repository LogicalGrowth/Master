import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Fun4FundTestModule } from '../../../test.module';
import { ExclusiveContentUpdateComponent } from 'app/entities/exclusive-content/exclusive-content-update.component';
import { ExclusiveContentService } from 'app/entities/exclusive-content/exclusive-content.service';
import { ExclusiveContent } from 'app/shared/model/exclusive-content.model';

describe('Component Tests', () => {
  describe('ExclusiveContent Management Update Component', () => {
    let comp: ExclusiveContentUpdateComponent;
    let fixture: ComponentFixture<ExclusiveContentUpdateComponent>;
    let service: ExclusiveContentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [ExclusiveContentUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ExclusiveContentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExclusiveContentUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExclusiveContentService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ExclusiveContent(123);
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
        const entity = new ExclusiveContent();
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
