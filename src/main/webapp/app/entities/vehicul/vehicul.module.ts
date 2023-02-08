import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SparkMeSharedModule } from 'app/shared/shared.module';
import { VehiculComponent } from './vehicul.component';
import { VehiculDetailComponent } from './vehicul-detail.component';
import { VehiculUpdateComponent } from './vehicul-update.component';
import { VehiculDeleteDialogComponent } from './vehicul-delete-dialog.component';
import { vehiculRoute } from './vehicul.route';

@NgModule({
  imports: [SparkMeSharedModule, RouterModule.forChild(vehiculRoute)],
  declarations: [VehiculComponent, VehiculDetailComponent, VehiculUpdateComponent, VehiculDeleteDialogComponent],
  entryComponents: [VehiculDeleteDialogComponent],
})
export class SparkMeVehiculModule {}
