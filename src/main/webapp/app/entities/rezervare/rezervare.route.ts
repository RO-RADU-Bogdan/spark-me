import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IRezervare, Rezervare } from 'app/shared/model/rezervare.model';
import { RezervareService } from './rezervare.service';
import { RezervareComponent } from './rezervare.component';
import { RezervareDetailComponent } from './rezervare-detail.component';
import { RezervareUpdateComponent } from './rezervare-update.component';

@Injectable({ providedIn: 'root' })
export class RezervareResolve implements Resolve<IRezervare> {
  constructor(private service: RezervareService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRezervare> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((rezervare: HttpResponse<Rezervare>) => {
          if (rezervare.body) {
            return of(rezervare.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Rezervare());
  }
}

export const rezervareRoute: Routes = [
  {
    path: '',
    component: RezervareComponent,
    data: {
      authorities: [Authority.USER, Authority.ADMIN],
      pageTitle: 'sparkMeApp.rezervare.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RezervareDetailComponent,
    resolve: {
      rezervare: RezervareResolve,
    },
    data: {
      authorities: [Authority.USER, Authority.ADMIN],
      pageTitle: 'sparkMeApp.rezervare.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RezervareUpdateComponent,
    resolve: {
      rezervare: RezervareResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'sparkMeApp.rezervare.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RezervareUpdateComponent,
    resolve: {
      rezervare: RezervareResolve,
    },
    data: {
      authorities: [],
      pageTitle: 'sparkMeApp.rezervare.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
