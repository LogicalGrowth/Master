import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Fun4FundTestModule } from '../../../test.module';
import { AuctionUpdateComponent } from 'app/entities/auction/auction-update.component';
import { AuctionService } from 'app/entities/auction/auction.service';
import { Auction } from 'app/shared/model/auction.model';

describe('Component Tests', () => {
  describe('Auction Management Update Component', () => {
    let comp: AuctionUpdateComponent;
    let fixture: ComponentFixture<AuctionUpdateComponent>;
    let service: AuctionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [AuctionUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(AuctionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AuctionUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AuctionService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Auction(123);
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
        const entity = new Auction();
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
