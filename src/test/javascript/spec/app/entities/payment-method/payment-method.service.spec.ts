import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { PaymentMethodService } from 'app/entities/payment-method/payment-method.service';
import { IPaymentMethod, PaymentMethod } from 'app/shared/model/payment-method.model';
import { CardType } from 'app/shared/model/enumerations/card-type.model';

describe('Service Tests', () => {
  describe('PaymentMethod Service', () => {
    let injector: TestBed;
    let service: PaymentMethodService;
    let httpMock: HttpTestingController;
    let elemDefault: IPaymentMethod;
    let expectedResult: IPaymentMethod | IPaymentMethod[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(PaymentMethodService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new PaymentMethod(0, 'AAAAAAA', 'AAAAAAA', currentDate, CardType.VISA, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            expirationDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a PaymentMethod', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            expirationDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            expirationDate: currentDate,
          },
          returnedFromService
        );

        service.create(new PaymentMethod()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PaymentMethod', () => {
        const returnedFromService = Object.assign(
          {
            cardNumber: 'BBBBBB',
            cardOwner: 'BBBBBB',
            expirationDate: currentDate.format(DATE_TIME_FORMAT),
            type: 'BBBBBB',
            cvc: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            expirationDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PaymentMethod', () => {
        const returnedFromService = Object.assign(
          {
            cardNumber: 'BBBBBB',
            cardOwner: 'BBBBBB',
            expirationDate: currentDate.format(DATE_TIME_FORMAT),
            type: 'BBBBBB',
            cvc: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            expirationDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a PaymentMethod', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
