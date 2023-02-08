import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRetea } from 'app/shared/model/retea.model';
import { ReteaService } from './retea.service';

@Component({
  templateUrl: './retea-delete-dialog.component.html',
})
export class ReteaDeleteDialogComponent {
  retea?: IRetea;

  constructor(protected reteaService: ReteaService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.reteaService.delete(id).subscribe(() => {
      this.eventManager.broadcast('reteaListModification');
      this.activeModal.close();
    });
  }
}
