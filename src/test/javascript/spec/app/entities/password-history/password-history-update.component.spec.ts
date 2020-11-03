import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Fun4FundTestModule } from '../../../test.module';
import { PasswordHistoryUpdateComponent } from 'app/entities/password-history/password-history-update.component';
import { PasswordHistoryService } from 'app/entities/password-history/password-history.service';
import { PasswordHistory } from 'app/shared/model/password-history.model';

describe('Component Tests', () => {
  describe('PasswordHistory Management Update Component', () => {
    let comp: PasswordHistoryUpdateComponent;
    let fixture: ComponentFixture<PasswordHistoryUpdateComponent>;
    let service: PasswordHistoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [PasswordHistoryUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PasswordHistoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PasswordHistoryUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PasswordHistoryService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PasswordHistory(123);
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
        const entity = new PasswordHistory();
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
