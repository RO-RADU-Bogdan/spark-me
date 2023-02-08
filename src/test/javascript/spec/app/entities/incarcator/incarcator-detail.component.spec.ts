import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SparkMeTestModule } from '../../../test.module';
import { IncarcatorDetailComponent } from 'app/entities/incarcator/incarcator-detail.component';
import { Incarcator } from 'app/shared/model/incarcator.model';

describe('Component Tests', () => {
  describe('Incarcator Management Detail Component', () => {
    let comp: IncarcatorDetailComponent;
    let fixture: ComponentFixture<IncarcatorDetailComponent>;
    const route = ({ data: of({ incarcator: new Incarcator(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SparkMeTestModule],
        declarations: [IncarcatorDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(IncarcatorDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IncarcatorDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load incarcator on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.incarcator).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
