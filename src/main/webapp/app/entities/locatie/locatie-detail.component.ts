import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILocatie } from 'app/shared/model/locatie.model';

import { faLocationArrow } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'jhi-locatie-detail',
  templateUrl: './locatie-detail.component.html',
})
export class LocatieDetailComponent implements OnInit {
  faLocationArrow = faLocationArrow;

  locatie: ILocatie | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ locatie }) => (this.locatie = locatie));
  }

  previousState(): void {
    window.history.back();
  }
}
