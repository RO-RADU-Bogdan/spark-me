import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { RezervareService } from 'app/entities/rezervare/rezervare.service';
import { IRezervare, Rezervare } from 'app/shared/model/rezervare.model';
import { StatutRezervare } from 'app/shared/model/enumerations/statut-rezervare.model';

describe('Service Tests', () => {
  describe('Rezervare Service', () => {
    let injector: TestBed;
    let service: RezervareService;
    let httpMock: HttpTestingController;
    let elemDefault: IRezervare;
    let expectedResult: IRezervare | IRezervare[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(RezervareService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Rezervare(0, currentDate, currentDate, currentDate, currentDate, StatutRezervare.NECUNOSCUT);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dataCreare: currentDate.format(DATE_TIME_FORMAT),
            dataExpirare: currentDate.format(DATE_TIME_FORMAT),
            dataStart: currentDate.format(DATE_TIME_FORMAT),
            dataFinal: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Rezervare', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dataCreare: currentDate.format(DATE_TIME_FORMAT),
            dataExpirare: currentDate.format(DATE_TIME_FORMAT),
            dataStart: currentDate.format(DATE_TIME_FORMAT),
            dataFinal: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataCreare: currentDate,
            dataExpirare: currentDate,
            dataStart: currentDate,
            dataFinal: currentDate,
          },
          returnedFromService
        );

        service.create(new Rezervare()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Rezervare', () => {
        const returnedFromService = Object.assign(
          {
            dataCreare: currentDate.format(DATE_TIME_FORMAT),
            dataExpirare: currentDate.format(DATE_TIME_FORMAT),
            dataStart: currentDate.format(DATE_TIME_FORMAT),
            dataFinal: currentDate.format(DATE_TIME_FORMAT),
            statut: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataCreare: currentDate,
            dataExpirare: currentDate,
            dataStart: currentDate,
            dataFinal: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Rezervare', () => {
        const returnedFromService = Object.assign(
          {
            dataCreare: currentDate.format(DATE_TIME_FORMAT),
            dataExpirare: currentDate.format(DATE_TIME_FORMAT),
            dataStart: currentDate.format(DATE_TIME_FORMAT),
            dataFinal: currentDate.format(DATE_TIME_FORMAT),
            statut: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataCreare: currentDate,
            dataExpirare: currentDate,
            dataStart: currentDate,
            dataFinal: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Rezervare', () => {
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
