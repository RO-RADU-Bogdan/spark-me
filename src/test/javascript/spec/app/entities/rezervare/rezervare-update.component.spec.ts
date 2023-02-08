import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SparkMeTestModule } from '../../../test.module';
import { RezervareUpdateComponent } from 'app/entities/rezervare/rezervare-update.component';
import { RezervareService } from 'app/entities/rezervare/rezervare.service';
import { Rezervare } from 'app/shared/model/rezervare.model';

describe('Component Tests', () => {
  describe('Rezervare Management Update Component', () => {
    let comp: RezervareUpdateComponent;
    let fixture: ComponentFixture<RezervareUpdateComponent>;
    let service: RezervareService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SparkMeTestModule],
        declarations: [RezervareUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(RezervareUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RezervareUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RezervareService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Rezervare(123);
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
        const entity = new Rezervare();
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
