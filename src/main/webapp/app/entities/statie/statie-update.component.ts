import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';

import { IStatie, Statie } from 'app/shared/model/statie.model';
import { StatieService } from './statie.service';
import { ILocatie } from 'app/shared/model/locatie.model';
import { LocatieService } from 'app/entities/locatie/locatie.service';
import { IRetea } from 'app/shared/model/retea.model';
import { ReteaService } from 'app/entities/retea/retea.service';
import { StatutStatie } from 'app/shared/model/enumerations/statut-statie.model';

type SelectableEntity = ILocatie | IRetea;

@Component({
  selector: 'jhi-statie-update',
  templateUrl: './statie-update.component.html',
})
export class StatieUpdateComponent implements OnInit {
  isSaving = false;
  locaties: ILocatie[] = [];
  reteas: IRetea[] = [];

  //
  create = true;
  //

  editForm = this.fb.group({
    id: [],
    denumire: [null, [Validators.required, Validators.maxLength(50)]],
    producator: [null, [Validators.required, Validators.maxLength(50)]],
    model: [null, [Validators.required, Validators.maxLength(50)]],
    latitudine: [null, [Validators.min(-90), Validators.max(90)]],
    longitudine: [null, [Validators.min(-180), Validators.max(180)]],
    statut: [null, [Validators.required]],
    tipCost: [null, [Validators.required]],
    descriereCost: [null, [Validators.maxLength(255)]],
    locatie: [],
    retea: [],
  });

  constructor(
    protected statieService: StatieService,
    protected locatieService: LocatieService,
    protected reteaService: ReteaService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ statie }) => {
      // Initializare:

      if (!statie.id) {
        statie.statut = StatutStatie.NECUNOSCUT;
        this.create = true;
      } else this.create = null;

      this.updateForm(statie);

      this.locatieService.query().subscribe((res: HttpResponse<ILocatie[]>) => (this.locaties = res.body || []));

      this.reteaService.query().subscribe((res: HttpResponse<IRetea[]>) => (this.reteas = res.body || []));
    });
  }

  updateForm(statie: IStatie): void {
    this.editForm.patchValue({
      id: statie.id,
      denumire: statie.denumire,
      producator: statie.producator,
      model: statie.model,
      latitudine: statie.latitudine,
      longitudine: statie.longitudine,
      statut: statie.statut,
      tipCost: statie.tipCost,
      descriereCost: statie.descriereCost,
      locatie: statie.locatie,
      retea: statie.retea,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const statie = this.createFromForm();
    if (statie.id !== undefined) {
      this.subscribeToSaveResponse(this.statieService.update(statie));
    } else {
      this.subscribeToSaveResponse(this.statieService.create(statie));
    }
  }

  private createFromForm(): IStatie {
    return {
      ...new Statie(),
      id: this.editForm.get(['id'])!.value,
      denumire: this.editForm.get(['denumire'])!.value,
      producator: this.editForm.get(['producator'])!.value,
      model: this.editForm.get(['model'])!.value,
      latitudine: this.editForm.get(['latitudine'])!.value,
      longitudine: this.editForm.get(['longitudine'])!.value,
      statut: this.editForm.get(['statut'])!.value,
      tipCost: this.editForm.get(['tipCost'])!.value,
      descriereCost: this.editForm.get(['descriereCost'])!.value,
      locatie: this.editForm.get(['locatie'])!.value,
      retea: this.editForm.get(['retea'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStatie>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    //this.previousState();
    this.router.navigate(['statie']);
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
