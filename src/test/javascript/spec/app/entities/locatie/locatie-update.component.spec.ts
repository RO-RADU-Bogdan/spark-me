import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SparkMeTestModule } from '../../../test.module';
import { LocatieUpdateComponent } from 'app/entities/locatie/locatie-update.component';
import { LocatieService } from 'app/entities/locatie/locatie.service';
import { Locatie } from 'app/shared/model/locatie.model';

describe('Component Tests', () => {
  describe('Locatie Management Update Component', () => {
    let comp: LocatieUpdateComponent;
    let fixture: ComponentFixture<LocatieUpdateComponent>;
    let service: LocatieService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SparkMeTestModule],
        declarations: [LocatieUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(LocatieUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LocatieUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LocatieService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Locatie(123);
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
        const entity = new Locatie();
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
