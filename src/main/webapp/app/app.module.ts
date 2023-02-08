import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { SparkMeSharedModule } from 'app/shared/shared.module';
import { SparkMeCoreModule } from 'app/core/core.module';
import { SparkMeAppRoutingModule } from './app-routing.module';
import { SparkMeHomeModule } from './home/home.module';
import { SparkMeEntityModule } from './entities/entity.module';
//
import { SparkMeMapModule } from './map/map.module';

// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';
import { AgmCoreModule } from '@agm/core';

@NgModule({
  imports: [
    BrowserModule,
    SparkMeSharedModule,
    SparkMeCoreModule,
    SparkMeHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    SparkMeEntityModule,
    SparkMeAppRoutingModule,
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyDPUTV5fssoZfoq5wX3Qhus1kCTTrMQCOA',
    }),
    SparkMeMapModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [MainComponent],
})
export class SparkMeAppModule {}
