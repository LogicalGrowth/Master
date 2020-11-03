import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Fun4FundTestModule } from '../../../test.module';
import { PaymentMethodDetailComponent } from 'app/entities/payment-method/payment-method-detail.component';
import { PaymentMethod } from 'app/shared/model/payment-method.model';

describe('Component Tests', () => {
  describe('PaymentMethod Management Detail Component', () => {
    let comp: PaymentMethodDetailComponent;
    let fixture: ComponentFixture<PaymentMethodDetailComponent>;
    const route = ({ data: of({ paymentMethod: new PaymentMethod(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Fun4FundTestModule],
        declarations: [PaymentMethodDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PaymentMethodDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PaymentMethodDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load paymentMethod on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.paymentMethod).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
