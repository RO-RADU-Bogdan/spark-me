import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { IncarcatorService } from 'app/entities/incarcator/incarcator.service';
import { IIncarcator, Incarcator } from 'app/shared/model/incarcator.model';
import { Conector } from 'app/shared/model/enumerations/conector.model';
import { Disponibilitate } from 'app/shared/model/enumerations/disponibilitate.model';

describe('Service Tests', () => {
  describe('Incarcator Service', () => {
    let injector: TestBed;
    let service: IncarcatorService;
    let httpMock: HttpTestingController;
    let elemDefault: IIncarcator;
    let expectedResult: IIncarcator | IIncarcator[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(IncarcatorService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Incarcator(0, 'AAAAAAA', Conector.TYPE1, 'AAAAAAA', Disponibilitate.NECUNOSCUT, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Incarcator', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Incarcator()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Incarcator', () => {
        const returnedFromService = Object.assign(
          {
            denumireConector: 'BBBBBB',
            conector: 'BBBBBB',
            descriereConector: 'BBBBBB',
            disponibilitate: 'BBBBBB',
            putere: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Incarcator', () => {
        const returnedFromService = Object.assign(
          {
            denumireConector: 'BBBBBB',
            conector: 'BBBBBB',
            descriereConector: 'BBBBBB',
            disponibilitate: 'BBBBBB',
            putere: 1,
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

      it('should delete a Incarcator', () => {
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
