import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Fun4FundTestModule } from '../../../test.module';
import { PrizeComponent } from 'app/entities/prize/prize.component';
import { PrizeService } from 'app/entities/prize/prize.service';
import { Prize } from 'app/shared/model/prize.model';

describe('Component Tests', () => {
  describe('Prize Management Component', () => {
    let comp: PrizeComponent;
    let fixture: ComponentFixture<PrizeComponent>;
    let service: PrizeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [PrizeComponent],
      })
        .overrideTemplate(PrizeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PrizeComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PrizeService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Prize(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.prizes && comp.prizes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
