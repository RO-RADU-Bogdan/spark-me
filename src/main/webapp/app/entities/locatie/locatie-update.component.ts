import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ILocatie, Locatie } from 'app/shared/model/locatie.model';
import { LocatieService } from './locatie.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-locatie-update',
  templateUrl: './locatie-update.component.html',
})
export class LocatieUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    denumire: [null, [Validators.required, Validators.maxLength(50)]],
    descriere: [null, [Validators.required, Validators.maxLength(255)]],
    latitudine: [null, [Validators.min(-90), Validators.max(90)]],
    longitudine: [null, [Validators.min(-180), Validators.max(180)]],
    tipAcces: [null, [Validators.required]],
    user: [],
  });

  //
  currentUser: IUser = {};

  constructor(
    protected locatieService: LocatieService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ locatie }) => {
      this.updateForm(locatie);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      // Primeste user logat curent:
      this.userService.findCurrentUser().subscribe(currentUser => (this.currentUser = currentUser));
      locatie.user = this.currentUser;
    });
  }

  updateForm(locatie: ILocatie): void {
    this.editForm.patchValue({
      id: locatie.id,
      denumire: locatie.denumire,
      descriere: locatie.descriere,
      latitudine: locatie.latitudine,
      longitudine: locatie.longitudine,
      tipAcces: locatie.tipAcces,
      user: locatie.user,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const locatie = this.createFromForm();
    if (locatie.id !== undefined) {
      this.subscribeToSaveResponse(this.locatieService.update(locatie));
    } else {
      this.subscribeToSaveResponse(this.locatieService.create(locatie));
    }
  }

  private createFromForm(): ILocatie {
    return {
      ...new Locatie(),
      id: this.editForm.get(['id'])!.value,
      denumire: this.editForm.get(['denumire'])!.value,
      descriere: this.editForm.get(['descriere'])!.value,
      latitudine: this.editForm.get(['latitudine'])!.value,
      longitudine: this.editForm.get(['longitudine'])!.value,
      tipAcces: this.editForm.get(['tipAcces'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocatie>>): void {
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
