import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { IncarcatorService } from 'app/entities/incarcator/incarcator.service';
import { IRezervare } from 'app/shared/model/rezervare.model';
import { RezervareService } from './rezervare.service';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { IIncarcator } from 'app/shared/model/incarcator.model';

@Component({
  templateUrl: './rezervare-delete-dialog.component.html',
})
export class RezervareDeleteDialogComponent implements OnInit {
  rezervare?: IRezervare;
  incarcatorRezervat: IIncarcator = {};
  isSaving = false;

  constructor(
    protected rezervareService: RezervareService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager,
    //
    protected incarcatorService: IncarcatorService
  ) {}

  ngOnInit(): void {
    // Comment
    this.incarcatorService.incarcatorRezervat = this.rezervare.incarcator;
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rezervareService.delete(id).subscribe(() => {
      this.eventManager.broadcast('rezervareListModification');
      //
      this.subscribeToSaveResponseIncarcatorRezervat(this.incarcatorService.updateEliberat());
      this.rezervareService.userAreRezervare = false;
      //
      this.activeModal.close();
    });
  }

  //
  protected subscribeToSaveResponseIncarcatorRezervat(result: Observable<HttpResponse<IIncarcator>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
  //
}
