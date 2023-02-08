import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { StatieService } from 'app/entities/statie/statie.service';
import { IStatie, Statie } from 'app/shared/model/statie.model';
import { StatutStatie } from 'app/shared/model/enumerations/statut-statie.model';
import { TipCost } from 'app/shared/model/enumerations/tip-cost.model';

describe('Service Tests', () => {
  describe('Statie Service', () => {
    let injector: TestBed;
    let service: StatieService;
    let httpMock: HttpTestingController;
    let elemDefault: IStatie;
    let expectedResult: IStatie | IStatie[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(StatieService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Statie(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 0, 0, StatutStatie.NECUNOSCUT, TipCost.NECUNOSCUT, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Statie', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Statie()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Statie', () => {
        const returnedFromService = Object.assign(
          {
            denumire: 'BBBBBB',
            producator: 'BBBBBB',
            model: 'BBBBBB',
            latitudine: 1,
            longitudine: 1,
            statut: 'BBBBBB',
            tipCost: 'BBBBBB',
            descriereCost: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Statie', () => {
        const returnedFromService = Object.assign(
          {
            denumire: 'BBBBBB',
            producator: 'BBBBBB',
            model: 'BBBBBB',
            latitudine: 1,
            longitudine: 1,
            statut: 'BBBBBB',
            tipCost: 'BBBBBB',
            descriereCost: 'BBBBBB',
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

      it('should delete a Statie', () => {
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
