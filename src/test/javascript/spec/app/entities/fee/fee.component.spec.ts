import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Fun4FundTestModule } from '../../../test.module';
import { FeeComponent } from 'app/entities/fee/fee.component';
import { FeeService } from 'app/entities/fee/fee.service';
import { Fee } from 'app/shared/model/fee.model';

describe('Component Tests', () => {
  describe('Fee Management Component', () => {
    let comp: FeeComponent;
    let fixture: ComponentFixture<FeeComponent>;
    let service: FeeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [FeeComponent],
      })
        .overrideTemplate(FeeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FeeComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FeeService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Fee(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.fees && comp.fees[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
