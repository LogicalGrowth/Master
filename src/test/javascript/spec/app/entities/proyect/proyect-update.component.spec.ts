import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Fun4FundTestModule } from '../../../test.module';
import { ProyectUpdateComponent } from 'app/entities/proyect/proyect-update.component';
import { ProyectService } from 'app/entities/proyect/proyect.service';
import { Proyect } from 'app/shared/model/proyect.model';

describe('Component Tests', () => {
  describe('Proyect Management Update Component', () => {
    let comp: ProyectUpdateComponent;
    let fixture: ComponentFixture<ProyectUpdateComponent>;
    let service: ProyectService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [ProyectUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ProyectUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProyectUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProyectService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Proyect(123);
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
        const entity = new Proyect();
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
