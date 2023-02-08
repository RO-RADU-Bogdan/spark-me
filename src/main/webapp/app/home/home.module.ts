import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SparkMeSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { AgmCoreModule } from '@agm/core';

@NgModule({
  imports: [
    SparkMeSharedModule,
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyDPUTV5fssoZfoq5wX3Qhus1kCTTrMQCOA',
    }),
    RouterModule.forChild([HOME_ROUTE]),
  ],
  declarations: [HomeComponent],
})
export class SparkMeHomeModule {}
