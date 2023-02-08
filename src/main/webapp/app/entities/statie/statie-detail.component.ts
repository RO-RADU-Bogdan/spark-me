import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { StatutRezervare } from 'app/shared/model/enumerations/statut-rezervare.model';
import { IIncarcator } from 'app/shared/model/incarcator.model';
import { IRezervare } from 'app/shared/model/rezervare.model';

import { IStatie } from 'app/shared/model/statie.model';
import * as moment from 'moment';
import { Moment } from 'moment';
import { IncarcatorService } from '../incarcator/incarcator.service';
import { RezervareService } from '../rezervare/rezervare.service';
import { StatieService } from './statie.service';
import { faChargingStation } from '@fortawesome/free-solid-svg-icons';
import { faBatteryThreeQuarters } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'jhi-statie-detail',
  templateUrl: './statie-detail.component.html',
})
export class StatieDetailComponent implements OnInit {
  faChargingStation = faChargingStation;
  faBatteryThreeQuarters = faBatteryThreeQuarters;

  statie: IStatie | null = null;

  
  incarcatorRezervat: IIncarcator = null;
  
  //
  currentUser: IUser = {};
  oneHour: Moment = null;
  //
  rezervariCurente: IRezervare[] = null;
  areRezervare = false;
  
  incarcatoare: IIncarcator[] | null = [];
  constructor(
    protected activatedRoute: ActivatedRoute,
    protected incarcatorService: IncarcatorService,
    protected rezervareService: RezervareService,
    protected userService: UserService,
    protected statieService: StatieService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ statie }) => (this.statie = statie));
    
    this.statieService.statieSelectata = this.statie;
    
    // Primeste toate incarcatoarele:
    this.incarcatorService.query().subscribe((res: HttpResponse<IIncarcator[]>) => (this.incarcatoare = res.body || []));

    // Primeste user logat curent:
    this.userService.findCurrentUser().subscribe(currentUser => (this.currentUser = currentUser));

    // Primeste rezervarile user-ului logat curent:
    this.rezervareService.findCurrentRezervari().subscribe(rezervariCurente => {
      this.rezervariCurente = rezervariCurente;

      this.oneHour = moment().startOf('minute').add('1', 'hours');

      for (const rezervare of rezervariCurente) {
        // Daca user are deja o rezervare in urmatoarea ora atunci acesta nu mai poate rezerva alte incarcatoare:
        if (
          moment(rezervare.dataStart).isBefore(this.oneHour) &&
          (rezervare.statut === StatutRezervare.CONFIRMAT || rezervare.statut === StatutRezervare.NECONFIRMAT)
        ) {
          this.rezervareService.userAreRezervare = true;
          this.areRezervare = this.rezervareService.userAreRezervare;
          break;
        } else {
          this.rezervareService.userAreRezervare = false;
          this.areRezervare = this.rezervareService.userAreRezervare;
        }
      }
    });
  }

  // ? Blocare buton de Book ?
  verificaDisponibilitate(incarcator: IIncarcator): boolean {
    let isDisponibil = true;

    if (incarcator.disponibilitate === 'DISPONIBIL') {
      isDisponibil = true;
    } else isDisponibil = false;
    return isDisponibil;
  }

  rezervaIncarcator(incarcator: IIncarcator): void {
    this.incarcatorService.rezervaIncarcator(incarcator);
  }
  //

  previousState(): void {
    window.history.back();
  }
}
