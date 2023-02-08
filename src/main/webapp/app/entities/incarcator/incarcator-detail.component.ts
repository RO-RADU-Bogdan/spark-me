import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIncarcator } from 'app/shared/model/incarcator.model';

import { faBatteryThreeQuarters } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'jhi-incarcator-detail',
  templateUrl: './incarcator-detail.component.html',
})
export class IncarcatorDetailComponent implements OnInit {
  faBatteryThreeQuarters = faBatteryThreeQuarters;

  incarcator: IIncarcator | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ incarcator }) => (this.incarcator = incarcator));
  }

  previousState(): void {
    window.history.back();
  }
}
