import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SparkMeSharedModule } from 'app/shared/shared.module';
import { IncarcatorComponent } from './incarcator.component';
import { IncarcatorDetailComponent } from './incarcator-detail.component';
import { IncarcatorUpdateComponent } from './incarcator-update.component';
import { IncarcatorDeleteDialogComponent } from './incarcator-delete-dialog.component';
import { incarcatorRoute } from './incarcator.route';

@NgModule({
  imports: [SparkMeSharedModule, RouterModule.forChild(incarcatorRoute)],
  declarations: [IncarcatorComponent, IncarcatorDetailComponent, IncarcatorUpdateComponent, IncarcatorDeleteDialogComponent],
  entryComponents: [IncarcatorDeleteDialogComponent],
})
export class SparkMeIncarcatorModule {}
