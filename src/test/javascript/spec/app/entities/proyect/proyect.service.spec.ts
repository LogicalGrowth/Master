import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ProyectService } from 'app/entities/proyect/proyect.service';
import { IProyect, Proyect } from 'app/shared/model/proyect.model';
import { ProyectType } from 'app/shared/model/enumerations/proyect-type.model';

describe('Service Tests', () => {
  describe('Proyect Service', () => {
    let injector: TestBed;
    let service: ProyectService;
    let httpMock: HttpTestingController;
    let elemDefault: IProyect;
    let expectedResult: IProyect | IProyect[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ProyectService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Proyect(0, 'AAAAAAA', 'AAAAAAA', ProyectType.PROFITABLE, 0, 0, 0, currentDate, currentDate, 0, 0, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Proyect', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            creationDate: currentDate,
            lastUpdated: currentDate,
          },
          returnedFromService
        );

        service.create(new Proyect()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Proyect', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            description: 'BBBBBB',
            idType: 'BBBBBB',
            goalAmount: 1,
            collected: 1,
            rating: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            coordX: 1,
            coordY: 1,
            fee: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            creationDate: currentDate,
            lastUpdated: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Proyect', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            description: 'BBBBBB',
            idType: 'BBBBBB',
            goalAmount: 1,
            collected: 1,
            rating: 1,
            creationDate: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            coordX: 1,
            coordY: 1,
            fee: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            creationDate: currentDate,
            lastUpdated: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Proyect', () => {
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
