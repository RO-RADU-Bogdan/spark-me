import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SparkMeTestModule } from '../../../test.module';
import { LocatieDetailComponent } from 'app/entities/locatie/locatie-detail.component';
import { Locatie } from 'app/shared/model/locatie.model';

describe('Component Tests', () => {
  describe('Locatie Management Detail Component', () => {
    let comp: LocatieDetailComponent;
    let fixture: ComponentFixture<LocatieDetailComponent>;
    const route = ({ data: of({ locatie: new Locatie(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SparkMeTestModule],
        declarations: [LocatieDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(LocatieDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LocatieDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load locatie on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.locatie).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
