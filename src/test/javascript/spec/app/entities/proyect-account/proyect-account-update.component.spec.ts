import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Fun4FundTestModule } from '../../../test.module';
import { ProyectAccountUpdateComponent } from 'app/entities/proyect-account/proyect-account-update.component';
import { ProyectAccountService } from 'app/entities/proyect-account/proyect-account.service';
import { ProyectAccount } from 'app/shared/model/proyect-account.model';

describe('Component Tests', () => {
  describe('ProyectAccount Management Update Component', () => {
    let comp: ProyectAccountUpdateComponent;
    let fixture: ComponentFixture<ProyectAccountUpdateComponent>;
    let service: ProyectAccountService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [ProyectAccountUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ProyectAccountUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProyectAccountUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProyectAccountService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ProyectAccount(123);
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
        const entity = new ProyectAccount();
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
