import { AgmCoreModule } from '@agm/core';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SparkMeSharedModule } from '../shared/shared.module';
import { MapComponent } from './map.component';
import { mapRoute } from './map.route';

@NgModule({
  imports: [
    SparkMeSharedModule,
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyDPUTV5fssoZfoq5wX3Qhus1kCTTrMQCOA',
    }),
    RouterModule.forChild([mapRoute]),
  ],
  declarations: [MapComponent],
  entryComponents: [],
})
export class SparkMeMapModule {}
