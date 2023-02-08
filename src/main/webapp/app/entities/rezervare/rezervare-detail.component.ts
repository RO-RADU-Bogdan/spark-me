import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRezervare } from 'app/shared/model/rezervare.model';
import * as moment from 'moment';
import { faBook } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'jhi-rezervare-detail',
  templateUrl: './rezervare-detail.component.html',
})
export class RezervareDetailComponent implements OnInit {
  faBook = faBook;

  rezervare: IRezervare | null = null;

  //
  dataCreareString: String = null;
  dataExpirareString: String = null;
  dataStartString: String = null;
  dataFinalString: String = null;
  //
  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rezervare }) => {
      // Formatare data
      this.dataCreareString = moment(rezervare.dataCreare).format('DD/MM/YYYY HH:mm');
      this.dataExpirareString = moment(rezervare.dataExpirare).format('DD/MM/YYYY HH:mm');
      this.dataStartString = moment(rezervare.dataStart).format('DD/MM/YYYY HH:mm');
      this.dataFinalString = moment(rezervare.dataFinal).format('DD/MM/YYYY HH:mm');

      this.rezervare = rezervare;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
