import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Fun4FundTestModule } from '../../../test.module';
import { DonationHistoryUpdateComponent } from 'app/entities/donation-history/donation-history-update.component';
import { DonationHistoryService } from 'app/entities/donation-history/donation-history.service';
import { DonationHistory } from 'app/shared/model/donation-history.model';

describe('Component Tests', () => {
  describe('DonationHistory Management Update Component', () => {
    let comp: DonationHistoryUpdateComponent;
    let fixture: ComponentFixture<DonationHistoryUpdateComponent>;
    let service: DonationHistoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [DonationHistoryUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(DonationHistoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DonationHistoryUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DonationHistoryService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new DonationHistory(123);
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
        const entity = new DonationHistory();
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
