import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { AuctionService } from 'app/entities/auction/auction.service';
import { IAuction, Auction } from 'app/shared/model/auction.model';
import { ActivityStatus } from 'app/shared/model/enumerations/activity-status.model';

describe('Service Tests', () => {
  describe('Auction Service', () => {
    let injector: TestBed;
    let service: AuctionService;
    let httpMock: HttpTestingController;
    let elemDefault: IAuction;
    let expectedResult: IAuction | IAuction[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(AuctionService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Auction(0, 0, 0, currentDate, ActivityStatus.ENABLED);
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

      it('should create a Auction', () => {
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

        service.create(new Auction()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Auction', () => {
        const returnedFromService = Object.assign(
          {
            initialBid: 1,
            winningBid: 1,
            expirationDate: currentDate.format(DATE_TIME_FORMAT),
            state: 'BBBBBB',
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

      it('should return a list of Auction', () => {
        const returnedFromService = Object.assign(
          {
            initialBid: 1,
            winningBid: 1,
            expirationDate: currentDate.format(DATE_TIME_FORMAT),
            state: 'BBBBBB',
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

      it('should delete a Auction', () => {
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
