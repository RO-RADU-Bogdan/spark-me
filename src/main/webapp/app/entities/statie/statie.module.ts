import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SparkMeSharedModule } from 'app/shared/shared.module';
import { StatieComponent } from './statie.component';
import { StatieDetailComponent } from './statie-detail.component';
import { StatieUpdateComponent } from './statie-update.component';
import { StatieDeleteDialogComponent } from './statie-delete-dialog.component';
import { statieRoute } from './statie.route';

@NgModule({
  imports: [SparkMeSharedModule, RouterModule.forChild(statieRoute)],
  declarations: [StatieComponent, StatieDetailComponent, StatieUpdateComponent, StatieDeleteDialogComponent],
  entryComponents: [StatieDeleteDialogComponent],
})
export class SparkMeStatieModule {}
