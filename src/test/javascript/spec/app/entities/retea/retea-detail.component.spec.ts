import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SparkMeTestModule } from '../../../test.module';
import { ReteaDetailComponent } from 'app/entities/retea/retea-detail.component';
import { Retea } from 'app/shared/model/retea.model';

describe('Component Tests', () => {
  describe('Retea Management Detail Component', () => {
    let comp: ReteaDetailComponent;
    let fixture: ComponentFixture<ReteaDetailComponent>;
    const route = ({ data: of({ retea: new Retea(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SparkMeTestModule],
        declarations: [ReteaDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ReteaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ReteaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load retea on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.retea).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
