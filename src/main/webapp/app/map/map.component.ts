import { Component, OnInit } from '@angular/core';
import { AgmCoreModule } from '@agm/core';
import { StatieService } from 'app/entities/statie/statie.service';
import { IStatie } from 'app/shared/model/statie.model';
import { Router } from '@angular/router';

import { faChargingStation } from '@fortawesome/free-solid-svg-icons';
import { faCircle } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'jhi-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss'],
})
export class MapComponent implements OnInit {
  faChargingStation = faChargingStation;
  faCircle = faCircle;

  statii: IStatie[] = null;
  lat: number;
  long: number;

  constructor(protected statieService: StatieService, protected router: Router) {}

  ngOnInit(): void {
    // Primeste lista statiilor:
    this.statieService.getListaStatii().subscribe(statii => {
      this.statii = statii;
      if (!this.statieService.statieSelectata) {
        this.lat = 44.43336939222885;
        this.long = 26.09795534403335;
      } else {
        this.lat = this.statieService.statieSelectata.latitudine;
        this.long = this.statieService.statieSelectata.longitudine;
      }
    });
    //
  }
}
