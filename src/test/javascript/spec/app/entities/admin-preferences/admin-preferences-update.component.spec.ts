import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Fun4FundTestModule } from '../../../test.module';
import { AdminPreferencesUpdateComponent } from 'app/entities/admin-preferences/admin-preferences-update.component';
import { AdminPreferencesService } from 'app/entities/admin-preferences/admin-preferences.service';
import { AdminPreferences } from 'app/shared/model/admin-preferences.model';

describe('Component Tests', () => {
  describe('AdminPreferences Management Update Component', () => {
    let comp: AdminPreferencesUpdateComponent;
    let fixture: ComponentFixture<AdminPreferencesUpdateComponent>;
    let service: AdminPreferencesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [AdminPreferencesUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(AdminPreferencesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AdminPreferencesUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AdminPreferencesService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new AdminPreferences(123);
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
        const entity = new AdminPreferences();
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
