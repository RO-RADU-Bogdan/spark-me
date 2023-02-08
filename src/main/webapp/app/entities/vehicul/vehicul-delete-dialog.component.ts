import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IVehicul } from 'app/shared/model/vehicul.model';
import { VehiculService } from './vehicul.service';

@Component({
  templateUrl: './vehicul-delete-dialog.component.html',
})
export class VehiculDeleteDialogComponent {
  vehicul?: IVehicul;

  constructor(protected vehiculService: VehiculService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vehiculService.delete(id).subscribe(() => {
      this.eventManager.broadcast('vehiculListModification');
      this.activeModal.close();
    });
  }
}
