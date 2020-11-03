import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ExclusiveContentService } from 'app/entities/exclusive-content/exclusive-content.service';
import { IExclusiveContent, ExclusiveContent } from 'app/shared/model/exclusive-content.model';
import { ActivityStatus } from 'app/shared/model/enumerations/activity-status.model';

describe('Service Tests', () => {
  describe('ExclusiveContent Service', () => {
    let injector: TestBed;
    let service: ExclusiveContentService;
    let httpMock: HttpTestingController;
    let elemDefault: IExclusiveContent;
    let expectedResult: IExclusiveContent | IExclusiveContent[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ExclusiveContentService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new ExclusiveContent(0, 0, 0, ActivityStatus.ENABLED);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ExclusiveContent', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ExclusiveContent()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ExclusiveContent', () => {
        const returnedFromService = Object.assign(
          {
            price: 1,
            stock: 1,
            state: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ExclusiveContent', () => {
        const returnedFromService = Object.assign(
          {
            price: 1,
            stock: 1,
            state: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ExclusiveContent', () => {
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
