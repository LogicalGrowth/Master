import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Fun4FundTestModule } from '../../../test.module';
import { AppLogUpdateComponent } from 'app/entities/app-log/app-log-update.component';
import { AppLogService } from 'app/entities/app-log/app-log.service';
import { AppLog } from 'app/shared/model/app-log.model';

describe('Component Tests', () => {
  describe('AppLog Management Update Component', () => {
    let comp: AppLogUpdateComponent;
    let fixture: ComponentFixture<AppLogUpdateComponent>;
    let service: AppLogService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [AppLogUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(AppLogUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AppLogUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AppLogService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new AppLog(123);
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
        const entity = new AppLog();
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
