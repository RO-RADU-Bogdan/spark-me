import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IVehicul, Vehicul } from 'app/shared/model/vehicul.model';
import { VehiculService } from './vehicul.service';
import { VehiculComponent } from './vehicul.component';
import { VehiculDetailComponent } from './vehicul-detail.component';
import { VehiculUpdateComponent } from './vehicul-update.component';

@Injectable({ providedIn: 'root' })
export class VehiculResolve implements Resolve<IVehicul> {
  constructor(private service: VehiculService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVehicul> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((vehicul: HttpResponse<Vehicul>) => {
          if (vehicul.body) {
            return of(vehicul.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Vehicul());
  }
}

export const vehiculRoute: Routes = [
  {
    path: '',
    component: VehiculComponent,
    data: {
      authorities: [Authority.USER, Authority.ADMIN],
      pageTitle: 'sparkMeApp.vehicul.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VehiculDetailComponent,
    resolve: {
      vehicul: VehiculResolve,
    },
    data: {
      authorities: [Authority.USER, Authority.ADMIN],
      pageTitle: 'sparkMeApp.vehicul.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VehiculUpdateComponent,
    resolve: {
      vehicul: VehiculResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'sparkMeApp.vehicul.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VehiculUpdateComponent,
    resolve: {
      vehicul: VehiculResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'sparkMeApp.vehicul.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
