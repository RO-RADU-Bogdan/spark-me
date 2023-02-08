import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SparkMeTestModule } from '../../../test.module';
import { ReteaUpdateComponent } from 'app/entities/retea/retea-update.component';
import { ReteaService } from 'app/entities/retea/retea.service';
import { Retea } from 'app/shared/model/retea.model';

describe('Component Tests', () => {
  describe('Retea Management Update Component', () => {
    let comp: ReteaUpdateComponent;
    let fixture: ComponentFixture<ReteaUpdateComponent>;
    let service: ReteaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SparkMeTestModule],
        declarations: [ReteaUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ReteaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReteaUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ReteaService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Retea(123);
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
        const entity = new Retea();
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
