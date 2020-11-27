import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Fun4FundTestModule } from '../../../test.module';
import { ConfigSystemUpdateComponent } from 'app/entities/config-system/config-system-update.component';
import { ConfigSystemService } from 'app/entities/config-system/config-system.service';
import { ConfigSystem } from 'app/shared/model/config-system.model';

describe('Component Tests', () => {
  describe('ConfigSystem Management Update Component', () => {
    let comp: ConfigSystemUpdateComponent;
    let fixture: ComponentFixture<ConfigSystemUpdateComponent>;
    let service: ConfigSystemService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [ConfigSystemUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ConfigSystemUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ConfigSystemUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ConfigSystemService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ConfigSystem(123);
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
        const entity = new ConfigSystem();
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
