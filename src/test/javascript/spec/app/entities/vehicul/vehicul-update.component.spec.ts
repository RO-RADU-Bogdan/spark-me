import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SparkMeTestModule } from '../../../test.module';
import { VehiculUpdateComponent } from 'app/entities/vehicul/vehicul-update.component';
import { VehiculService } from 'app/entities/vehicul/vehicul.service';
import { Vehicul } from 'app/shared/model/vehicul.model';

describe('Component Tests', () => {
  describe('Vehicul Management Update Component', () => {
    let comp: VehiculUpdateComponent;
    let fixture: ComponentFixture<VehiculUpdateComponent>;
    let service: VehiculService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SparkMeTestModule],
        declarations: [VehiculUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(VehiculUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VehiculUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(VehiculService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Vehicul(123);
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
        const entity = new Vehicul();
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
