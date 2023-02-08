import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SparkMeSharedModule } from 'app/shared/shared.module';
import { LocatieComponent } from './locatie.component';
import { LocatieDetailComponent } from './locatie-detail.component';
import { LocatieUpdateComponent } from './locatie-update.component';
import { LocatieDeleteDialogComponent } from './locatie-delete-dialog.component';
import { locatieRoute } from './locatie.route';

@NgModule({
  imports: [SparkMeSharedModule, RouterModule.forChild(locatieRoute)],
  declarations: [LocatieComponent, LocatieDetailComponent, LocatieUpdateComponent, LocatieDeleteDialogComponent],
  entryComponents: [LocatieDeleteDialogComponent],
})
export class SparkMeLocatieModule {}
