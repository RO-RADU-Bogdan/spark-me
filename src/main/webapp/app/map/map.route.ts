import { Route } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { MapComponent } from './map.component';

export const mapRoute: Route = {
  path: '',
  component: MapComponent,
  data: {
    authorities: [],
    pageTitle: 'sparkMeApp.map.home.title',
  },
  canActivate: [UserRouteAccessService],
};
