import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Fun4FundTestModule } from '../../../test.module';
import { RaffleUpdateComponent } from 'app/entities/raffle/raffle-update.component';
import { RaffleService } from 'app/entities/raffle/raffle.service';
import { Raffle } from 'app/shared/model/raffle.model';

describe('Component Tests', () => {
  describe('Raffle Management Update Component', () => {
    let comp: RaffleUpdateComponent;
    let fixture: ComponentFixture<RaffleUpdateComponent>;
    let service: RaffleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [RaffleUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(RaffleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RaffleUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RaffleService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Raffle(123);
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
        const entity = new Raffle();
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
