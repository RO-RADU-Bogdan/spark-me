import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVehicul } from 'app/shared/model/vehicul.model';
import { faCar } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'jhi-vehicul-detail',
  templateUrl: './vehicul-detail.component.html',
})
export class VehiculDetailComponent implements OnInit {
  faCar = faCar;

  vehicul: IVehicul | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vehicul }) => (this.vehicul = vehicul));
  }

  previousState(): void {
    window.history.back();
  }
}
