import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IIncarcator } from 'app/shared/model/incarcator.model';
import { IncarcatorService } from './incarcator.service';

@Component({
  templateUrl: './incarcator-delete-dialog.component.html',
})
export class IncarcatorDeleteDialogComponent {
  incarcator?: IIncarcator;

  constructor(
    protected incarcatorService: IncarcatorService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.incarcatorService.delete(id).subscribe(() => {
      this.eventManager.broadcast('incarcatorListModification');
      this.activeModal.close();
    });
  }
}
