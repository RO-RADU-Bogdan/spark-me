import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SparkMeTestModule } from '../../../test.module';
import { RezervareDetailComponent } from 'app/entities/rezervare/rezervare-detail.component';
import { Rezervare } from 'app/shared/model/rezervare.model';

describe('Component Tests', () => {
  describe('Rezervare Management Detail Component', () => {
    let comp: RezervareDetailComponent;
    let fixture: ComponentFixture<RezervareDetailComponent>;
    const route = ({ data: of({ rezervare: new Rezervare(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SparkMeTestModule],
        declarations: [RezervareDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(RezervareDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RezervareDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load rezervare on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.rezervare).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
