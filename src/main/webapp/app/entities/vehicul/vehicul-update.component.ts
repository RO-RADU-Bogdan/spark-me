import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IVehicul, Vehicul } from 'app/shared/model/vehicul.model';
import { VehiculService } from './vehicul.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-vehicul-update',
  templateUrl: './vehicul-update.component.html',
})
export class VehiculUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    marca: [null, [Validators.required, Validators.maxLength(50)]],
    model: [null, [Validators.required, Validators.maxLength(50)]],
    anFabricatie: [null, [Validators.required, Validators.min(1990), Validators.max(2021)]],
    culoare: [null, [Validators.required, Validators.maxLength(50)]],
    descriere: [null, [Validators.maxLength(255)]],
    conector: [null, [Validators.required]],
    user: [],
  });

  //
  currentUser: IUser = {};

  constructor(
    protected vehiculService: VehiculService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vehicul }) => {
      this.updateForm(vehicul);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      // Primeste user logat curent:
      this.userService.findCurrentUser().subscribe(currentUser => (this.currentUser = currentUser));
      vehicul.user = this.currentUser;
    });
  }

  updateForm(vehicul: IVehicul): void {
    this.editForm.patchValue({
      id: vehicul.id,
      marca: vehicul.marca,
      model: vehicul.model,
      anFabricatie: vehicul.anFabricatie,
      culoare: vehicul.culoare,
      descriere: vehicul.descriere,
      conector: vehicul.conector,
      user: vehicul.user,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vehicul = this.createFromForm();
    if (vehicul.id !== undefined) {
      this.subscribeToSaveResponse(this.vehiculService.update(vehicul));
    } else {
      this.subscribeToSaveResponse(this.vehiculService.create(vehicul));
    }
  }

  private createFromForm(): IVehicul {
    return {
      ...new Vehicul(),
      id: this.editForm.get(['id'])!.value,
      marca: this.editForm.get(['marca'])!.value,
      model: this.editForm.get(['model'])!.value,
      anFabricatie: this.editForm.get(['anFabricatie'])!.value,
      culoare: this.editForm.get(['culoare'])!.value,
      descriere: this.editForm.get(['descriere'])!.value,
      conector: this.editForm.get(['conector'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVehicul>>): void {
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

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
