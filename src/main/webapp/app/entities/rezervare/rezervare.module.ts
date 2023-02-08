import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SparkMeSharedModule } from 'app/shared/shared.module';
import { RezervareComponent } from './rezervare.component';
import { RezervareDetailComponent } from './rezervare-detail.component';
import { RezervareUpdateComponent } from './rezervare-update.component';
import { RezervareDeleteDialogComponent } from './rezervare-delete-dialog.component';
import { rezervareRoute } from './rezervare.route';

@NgModule({
  imports: [SparkMeSharedModule, RouterModule.forChild(rezervareRoute)],
  declarations: [RezervareComponent, RezervareDetailComponent, RezervareUpdateComponent, RezervareDeleteDialogComponent],
  entryComponents: [RezervareDeleteDialogComponent],
})
export class SparkMeRezervareModule {}
