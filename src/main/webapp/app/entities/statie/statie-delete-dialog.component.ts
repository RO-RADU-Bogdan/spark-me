import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStatie } from 'app/shared/model/statie.model';
import { StatieService } from './statie.service';

@Component({
  templateUrl: './statie-delete-dialog.component.html',
})
export class StatieDeleteDialogComponent {
  statie?: IStatie;

  constructor(protected statieService: StatieService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.statieService.delete(id).subscribe(() => {
      this.eventManager.broadcast('statieListModification');
      this.activeModal.close();
    });
  }
}
