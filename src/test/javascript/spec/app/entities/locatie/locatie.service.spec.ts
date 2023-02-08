import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LocatieService } from 'app/entities/locatie/locatie.service';
import { ILocatie, Locatie } from 'app/shared/model/locatie.model';
import { TipAcces } from 'app/shared/model/enumerations/tip-acces.model';

describe('Service Tests', () => {
  describe('Locatie Service', () => {
    let injector: TestBed;
    let service: LocatieService;
    let httpMock: HttpTestingController;
    let elemDefault: ILocatie;
    let expectedResult: ILocatie | ILocatie[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(LocatieService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Locatie(0, 'AAAAAAA', 'AAAAAAA', 0, 0, TipAcces.PUBLIC);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Locatie', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Locatie()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Locatie', () => {
        const returnedFromService = Object.assign(
          {
            denumire: 'BBBBBB',
            descriere: 'BBBBBB',
            latitudine: 1,
            longitudine: 1,
            tipAcces: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Locatie', () => {
        const returnedFromService = Object.assign(
          {
            denumire: 'BBBBBB',
            descriere: 'BBBBBB',
            latitudine: 1,
            longitudine: 1,
            tipAcces: 'BBBBBB',
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

      it('should delete a Locatie', () => {
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
