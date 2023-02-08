import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IStatie, Statie } from 'app/shared/model/statie.model';
import { StatieService } from './statie.service';
import { StatieComponent } from './statie.component';
import { StatieDetailComponent } from './statie-detail.component';
import { StatieUpdateComponent } from './statie-update.component';

@Injectable({ providedIn: 'root' })
export class StatieResolve implements Resolve<IStatie> {
  constructor(private service: StatieService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStatie> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((statie: HttpResponse<Statie>) => {
          if (statie.body) {
            return of(statie.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Statie());
  }
}

export const statieRoute: Routes = [
  {
    path: '',
    component: StatieComponent,
    data: {
      authorities: [Authority.USER, Authority.ADMIN],
      pageTitle: 'sparkMeApp.statie.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StatieDetailComponent,
    resolve: {
      statie: StatieResolve,
    },
    data: {
      authorities: [Authority.USER, Authority.ADMIN],
      pageTitle: 'sparkMeApp.statie.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StatieUpdateComponent,
    resolve: {
      statie: StatieResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'sparkMeApp.statie.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StatieUpdateComponent,
    resolve: {
      statie: StatieResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'sparkMeApp.statie.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
