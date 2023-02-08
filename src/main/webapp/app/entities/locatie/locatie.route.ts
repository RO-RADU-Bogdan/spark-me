import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ILocatie, Locatie } from 'app/shared/model/locatie.model';
import { LocatieService } from './locatie.service';
import { LocatieComponent } from './locatie.component';
import { LocatieDetailComponent } from './locatie-detail.component';
import { LocatieUpdateComponent } from './locatie-update.component';

@Injectable({ providedIn: 'root' })
export class LocatieResolve implements Resolve<ILocatie> {
  constructor(private service: LocatieService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILocatie> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((locatie: HttpResponse<Locatie>) => {
          if (locatie.body) {
            return of(locatie.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Locatie());
  }
}

export const locatieRoute: Routes = [
  {
    path: '',
    component: LocatieComponent,
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'sparkMeApp.locatie.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LocatieDetailComponent,
    resolve: {
      locatie: LocatieResolve,
    },
    data: {
      authorities: [Authority.USER, Authority.ADMIN],
      pageTitle: 'sparkMeApp.locatie.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LocatieUpdateComponent,
    resolve: {
      locatie: LocatieResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'sparkMeApp.locatie.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LocatieUpdateComponent,
    resolve: {
      locatie: LocatieResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'sparkMeApp.locatie.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
