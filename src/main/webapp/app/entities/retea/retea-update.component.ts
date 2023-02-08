import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IRetea, Retea } from 'app/shared/model/retea.model';
import { ReteaService } from './retea.service';

@Component({
  selector: 'jhi-retea-update',
  templateUrl: './retea-update.component.html',
})
export class ReteaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    denumire: [null, [Validators.required, Validators.maxLength(50)]],
    descriere: [null, [Validators.maxLength(255)]],
  });

  constructor(protected reteaService: ReteaService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ retea }) => {
      this.updateForm(retea);
    });
  }

  updateForm(retea: IRetea): void {
    this.editForm.patchValue({
      id: retea.id,
      denumire: retea.denumire,
      descriere: retea.descriere,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const retea = this.createFromForm();
    if (retea.id !== undefined) {
      this.subscribeToSaveResponse(this.reteaService.update(retea));
    } else {
      this.subscribeToSaveResponse(this.reteaService.create(retea));
    }
  }

  private createFromForm(): IRetea {
    return {
      ...new Retea(),
      id: this.editForm.get(['id'])!.value,
      denumire: this.editForm.get(['denumire'])!.value,
      descriere: this.editForm.get(['descriere'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRetea>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
