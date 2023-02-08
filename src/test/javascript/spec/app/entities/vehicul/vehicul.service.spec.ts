import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { VehiculService } from 'app/entities/vehicul/vehicul.service';
import { IVehicul, Vehicul } from 'app/shared/model/vehicul.model';
import { Conector } from 'app/shared/model/enumerations/conector.model';

describe('Service Tests', () => {
  describe('Vehicul Service', () => {
    let injector: TestBed;
    let service: VehiculService;
    let httpMock: HttpTestingController;
    let elemDefault: IVehicul;
    let expectedResult: IVehicul | IVehicul[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(VehiculService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Vehicul(0, 'AAAAAAA', 'AAAAAAA', 0, 'AAAAAAA', 'AAAAAAA', Conector.TYPE1);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Vehicul', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Vehicul()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Vehicul', () => {
        const returnedFromService = Object.assign(
          {
            marca: 'BBBBBB',
            model: 'BBBBBB',
            anFabricatie: 1,
            culoare: 'BBBBBB',
            descriere: 'BBBBBB',
            conector: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Vehicul', () => {
        const returnedFromService = Object.assign(
          {
            marca: 'BBBBBB',
            model: 'BBBBBB',
            anFabricatie: 1,
            culoare: 'BBBBBB',
            descriere: 'BBBBBB',
            conector: 'BBBBBB',
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

      it('should delete a Vehicul', () => {
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
