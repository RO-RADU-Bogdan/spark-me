import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IIncarcator, Incarcator } from 'app/shared/model/incarcator.model';
import { IncarcatorService } from './incarcator.service';
import { IncarcatorComponent } from './incarcator.component';
import { IncarcatorDetailComponent } from './incarcator-detail.component';
import { IncarcatorUpdateComponent } from './incarcator-update.component';

@Injectable({ providedIn: 'root' })
export class IncarcatorResolve implements Resolve<IIncarcator> {
  constructor(private service: IncarcatorService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIncarcator> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((incarcator: HttpResponse<Incarcator>) => {
          if (incarcator.body) {
            return of(incarcator.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Incarcator());
  }
}

export const incarcatorRoute: Routes = [
  {
    path: '',
    component: IncarcatorComponent,
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'sparkMeApp.incarcator.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IncarcatorDetailComponent,
    resolve: {
      incarcator: IncarcatorResolve,
    },
    data: {
      authorities: [Authority.USER, Authority.ADMIN],
      pageTitle: 'sparkMeApp.incarcator.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IncarcatorUpdateComponent,
    resolve: {
      incarcator: IncarcatorResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'sparkMeApp.incarcator.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IncarcatorUpdateComponent,
    resolve: {
      incarcator: IncarcatorResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'sparkMeApp.incarcator.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
