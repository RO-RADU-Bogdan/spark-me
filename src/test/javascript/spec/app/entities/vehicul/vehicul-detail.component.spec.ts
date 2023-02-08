import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SparkMeTestModule } from '../../../test.module';
import { VehiculDetailComponent } from 'app/entities/vehicul/vehicul-detail.component';
import { Vehicul } from 'app/shared/model/vehicul.model';

describe('Component Tests', () => {
  describe('Vehicul Management Detail Component', () => {
    let comp: VehiculDetailComponent;
    let fixture: ComponentFixture<VehiculDetailComponent>;
    const route = ({ data: of({ vehicul: new Vehicul(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SparkMeTestModule],
        declarations: [VehiculDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(VehiculDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VehiculDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load vehicul on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.vehicul).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
