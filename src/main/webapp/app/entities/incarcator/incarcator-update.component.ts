import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';

import { IIncarcator, Incarcator } from 'app/shared/model/incarcator.model';
import { IncarcatorService } from './incarcator.service';
import { IStatie } from 'app/shared/model/statie.model';
import { StatieService } from 'app/entities/statie/statie.service';
import { Disponibilitate } from 'app/shared/model/enumerations/disponibilitate.model';

@Component({
  selector: 'jhi-incarcator-update',
  templateUrl: './incarcator-update.component.html',
})
export class IncarcatorUpdateComponent implements OnInit {
  isSaving = false;
  staties: IStatie[] = [];

  //
  inc: IIncarcator = null;
  create = true;
  //

  editForm = this.fb.group({
    id: [],
    denumireConector: [null, [Validators.required, Validators.maxLength(55)]],
    conector: [null, [Validators.required]],
    descriereConector: [null, [Validators.maxLength(255)]],
    disponibilitate: [null, [Validators.required]],
    putere: [null, [Validators.required, Validators.min(0), Validators.max(999)]],
    statie: [],
  });

  constructor(
    protected incarcatorService: IncarcatorService,
    protected statieService: StatieService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ incarcator }) => {
      //
      if (!incarcator.id) {
        incarcator.disponibilitate = Disponibilitate.NECUNOSCUT;
        this.create = true;
      } else this.create = null;

      this.updateForm(incarcator);

      this.statieService.query().subscribe((res: HttpResponse<IStatie[]>) => (this.staties = res.body || []));
    });
  }

  updateForm(incarcator: IIncarcator): void {
    this.editForm.patchValue({
      id: incarcator.id,
      denumireConector: incarcator.denumireConector,
      conector: incarcator.conector,
      descriereConector: incarcator.descriereConector,
      disponibilitate: incarcator.disponibilitate,
      putere: incarcator.putere,
      statie: incarcator.statie,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const incarcator = this.createFromForm();
    this.inc = incarcator;
    if (incarcator.id !== undefined) {
      this.subscribeToSaveResponse(this.incarcatorService.update(incarcator));
    } else {
      this.subscribeToSaveResponse(this.incarcatorService.create(incarcator));
    }
  }

  private createFromForm(): IIncarcator {
    return {
      ...new Incarcator(),
      id: this.editForm.get(['id'])!.value,
      denumireConector: this.editForm.get(['denumireConector'])!.value,
      conector: this.editForm.get(['conector'])!.value,
      descriereConector: this.editForm.get(['descriereConector'])!.value,
      disponibilitate: this.editForm.get(['disponibilitate'])!.value,
      putere: this.editForm.get(['putere'])!.value,
      statie: this.editForm.get(['statie'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIncarcator>>): void {
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

  trackById(index: number, item: IStatie): any {
    return item.id;
  }
}
