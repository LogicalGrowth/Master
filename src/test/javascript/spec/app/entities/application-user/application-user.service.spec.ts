import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ApplicationUserService } from 'app/entities/application-user/application-user.service';
import { IApplicationUser, ApplicationUser } from 'app/shared/model/application-user.model';
import { IdType } from 'app/shared/model/enumerations/id-type.model';

describe('Service Tests', () => {
  describe('ApplicationUser Service', () => {
    let injector: TestBed;
    let service: ApplicationUserService;
    let httpMock: HttpTestingController;
    let elemDefault: IApplicationUser;
    let expectedResult: IApplicationUser | IApplicationUser[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ApplicationUserService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new ApplicationUser(0, 'AAAAAAA', IdType.PASSPORT, currentDate, 'AAAAAAA', false);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            birthDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ApplicationUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            birthDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            birthDate: currentDate,
          },
          returnedFromService
        );

        service.create(new ApplicationUser()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ApplicationUser', () => {
        const returnedFromService = Object.assign(
          {
            identification: 'BBBBBB',
            idType: 'BBBBBB',
            birthDate: currentDate.format(DATE_TIME_FORMAT),
            phoneNumber: 'BBBBBB',
            admin: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            birthDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ApplicationUser', () => {
        const returnedFromService = Object.assign(
          {
            identification: 'BBBBBB',
            idType: 'BBBBBB',
            birthDate: currentDate.format(DATE_TIME_FORMAT),
            phoneNumber: 'BBBBBB',
            admin: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            birthDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ApplicationUser', () => {
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
