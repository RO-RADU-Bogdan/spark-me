import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRetea } from 'app/shared/model/retea.model';
import { faUsers } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'jhi-retea-detail',
  templateUrl: './retea-detail.component.html',
})
export class ReteaDetailComponent implements OnInit {
  faUsers = faUsers;

  retea: IRetea | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ retea }) => (this.retea = retea));
  }

  previousState(): void {
    window.history.back();
  }
}
