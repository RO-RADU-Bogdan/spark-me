import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILocatie } from 'app/shared/model/locatie.model';
import { LocatieService } from './locatie.service';

@Component({
  templateUrl: './locatie-delete-dialog.component.html',
})
export class LocatieDeleteDialogComponent {
  locatie?: ILocatie;

  constructor(protected locatieService: LocatieService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.locatieService.delete(id).subscribe(() => {
      this.eventManager.broadcast('locatieListModification');
      this.activeModal.close();
    });
  }
}
