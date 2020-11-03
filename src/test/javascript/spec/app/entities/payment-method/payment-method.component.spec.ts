import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Fun4FundTestModule } from '../../../test.module';
import { PaymentMethodComponent } from 'app/entities/payment-method/payment-method.component';
import { PaymentMethodService } from 'app/entities/payment-method/payment-method.service';
import { PaymentMethod } from 'app/shared/model/payment-method.model';

describe('Component Tests', () => {
  describe('PaymentMethod Management Component', () => {
    let comp: PaymentMethodComponent;
    let fixture: ComponentFixture<PaymentMethodComponent>;
    let service: PaymentMethodService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [PaymentMethodComponent],
      })
        .overrideTemplate(PaymentMethodComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PaymentMethodComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PaymentMethodService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new PaymentMethod(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.paymentMethods && comp.paymentMethods[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
