import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Fun4FundTestModule } from '../../../test.module';
import { CheckpointUpdateComponent } from 'app/entities/checkpoint/checkpoint-update.component';
import { CheckpointService } from 'app/entities/checkpoint/checkpoint.service';
import { Checkpoint } from 'app/shared/model/checkpoint.model';

describe('Component Tests', () => {
  describe('Checkpoint Management Update Component', () => {
    let comp: CheckpointUpdateComponent;
    let fixture: ComponentFixture<CheckpointUpdateComponent>;
    let service: CheckpointService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [CheckpointUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(CheckpointUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CheckpointUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CheckpointService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Checkpoint(123);
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
        const entity = new Checkpoint();
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
