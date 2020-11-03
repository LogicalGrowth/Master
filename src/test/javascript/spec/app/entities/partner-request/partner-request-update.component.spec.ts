import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Fun4FundTestModule } from '../../../test.module';
import { PartnerRequestUpdateComponent } from 'app/entities/partner-request/partner-request-update.component';
import { PartnerRequestService } from 'app/entities/partner-request/partner-request.service';
import { PartnerRequest } from 'app/shared/model/partner-request.model';

describe('Component Tests', () => {
  describe('PartnerRequest Management Update Component', () => {
    let comp: PartnerRequestUpdateComponent;
    let fixture: ComponentFixture<PartnerRequestUpdateComponent>;
    let service: PartnerRequestService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [PartnerRequestUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PartnerRequestUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PartnerRequestUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PartnerRequestService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PartnerRequest(123);
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
        const entity = new PartnerRequest();
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
