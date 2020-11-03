import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Fun4FundTestModule } from '../../../test.module';
import { RaffleComponent } from 'app/entities/raffle/raffle.component';
import { RaffleService } from 'app/entities/raffle/raffle.service';
import { Raffle } from 'app/shared/model/raffle.model';

describe('Component Tests', () => {
  describe('Raffle Management Component', () => {
    let comp: RaffleComponent;
    let fixture: ComponentFixture<RaffleComponent>;
    let service: RaffleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [RaffleComponent],
      })
        .overrideTemplate(RaffleComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RaffleComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RaffleService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Raffle(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.raffles && comp.raffles[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
