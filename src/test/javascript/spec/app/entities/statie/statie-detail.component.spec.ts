import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SparkMeTestModule } from '../../../test.module';
import { StatieDetailComponent } from 'app/entities/statie/statie-detail.component';
import { Statie } from 'app/shared/model/statie.model';

describe('Component Tests', () => {
  describe('Statie Management Detail Component', () => {
    let comp: StatieDetailComponent;
    let fixture: ComponentFixture<StatieDetailComponent>;
    const route = ({ data: of({ statie: new Statie(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SparkMeTestModule],
        declarations: [StatieDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(StatieDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StatieDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load statie on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.statie).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
