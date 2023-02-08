import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IRetea, Retea } from 'app/shared/model/retea.model';
import { ReteaService } from './retea.service';
import { ReteaComponent } from './retea.component';
import { ReteaDetailComponent } from './retea-detail.component';
import { ReteaUpdateComponent } from './retea-update.component';

@Injectable({ providedIn: 'root' })
export class ReteaResolve implements Resolve<IRetea> {
  constructor(private service: ReteaService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRetea> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((retea: HttpResponse<Retea>) => {
          if (retea.body) {
            return of(retea.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Retea());
  }
}

export const reteaRoute: Routes = [
  {
    path: '',
    component: ReteaComponent,
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'sparkMeApp.retea.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReteaDetailComponent,
    resolve: {
      retea: ReteaResolve,
    },
    data: {
      authorities: [Authority.USER, Authority.ADMIN],
      pageTitle: 'sparkMeApp.retea.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReteaUpdateComponent,
    resolve: {
      retea: ReteaResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'sparkMeApp.retea.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReteaUpdateComponent,
    resolve: {
      retea: ReteaResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'sparkMeApp.retea.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
