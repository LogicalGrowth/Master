import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { AppLogService } from 'app/entities/app-log/app-log.service';
import { IAppLog, AppLog } from 'app/shared/model/app-log.model';
import { ActionType } from 'app/shared/model/enumerations/action-type.model';

describe('Service Tests', () => {
  describe('AppLog Service', () => {
    let injector: TestBed;
    let service: AppLogService;
    let httpMock: HttpTestingController;
    let elemDefault: IAppLog;
    let expectedResult: IAppLog | IAppLog[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(AppLogService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new AppLog(0, currentDate, ActionType.CREATE, 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            timeStamp: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a AppLog', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            timeStamp: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            timeStamp: currentDate,
          },
          returnedFromService
        );

        service.create(new AppLog()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AppLog', () => {
        const returnedFromService = Object.assign(
          {
            timeStamp: currentDate.format(DATE_TIME_FORMAT),
            action: 'BBBBBB',
            user: 'BBBBBB',
            description: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            timeStamp: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AppLog', () => {
        const returnedFromService = Object.assign(
          {
            timeStamp: currentDate.format(DATE_TIME_FORMAT),
            action: 'BBBBBB',
            user: 'BBBBBB',
            description: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            timeStamp: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a AppLog', () => {
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
