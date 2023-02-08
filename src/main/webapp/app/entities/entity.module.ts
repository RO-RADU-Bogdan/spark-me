import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'vehicul',
        loadChildren: () => import('./vehicul/vehicul.module').then(m => m.SparkMeVehiculModule),
      },
      {
        path: 'rezervare',
        loadChildren: () => import('./rezervare/rezervare.module').then(m => m.SparkMeRezervareModule),
      },
      {
        path: 'locatie',
        loadChildren: () => import('./locatie/locatie.module').then(m => m.SparkMeLocatieModule),
      },
      {
        path: 'statie',
        loadChildren: () => import('./statie/statie.module').then(m => m.SparkMeStatieModule),
      },
      {
        path: 'incarcator',
        loadChildren: () => import('./incarcator/incarcator.module').then(m => m.SparkMeIncarcatorModule),
      },
      {
        path: 'retea',
        loadChildren: () => import('./retea/retea.module').then(m => m.SparkMeReteaModule),
      },
      {
        path: 'map',
        loadChildren: () => import('../map/map.module').then(m => m.SparkMeMapModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class SparkMeEntityModule {}
