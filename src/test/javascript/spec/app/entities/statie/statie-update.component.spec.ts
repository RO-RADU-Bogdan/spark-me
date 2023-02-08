import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SparkMeTestModule } from '../../../test.module';
import { StatieUpdateComponent } from 'app/entities/statie/statie-update.component';
import { StatieService } from 'app/entities/statie/statie.service';
import { Statie } from 'app/shared/model/statie.model';

describe('Component Tests', () => {
  describe('Statie Management Update Component', () => {
    let comp: StatieUpdateComponent;
    let fixture: ComponentFixture<StatieUpdateComponent>;
    let service: StatieService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SparkMeTestModule],
        declarations: [StatieUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(StatieUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StatieUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(StatieService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Statie(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Statie();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
