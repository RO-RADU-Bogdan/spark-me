import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SparkMeSharedModule } from 'app/shared/shared.module';
import { ReteaComponent } from './retea.component';
import { ReteaDetailComponent } from './retea-detail.component';
import { ReteaUpdateComponent } from './retea-update.component';
import { ReteaDeleteDialogComponent } from './retea-delete-dialog.component';
import { reteaRoute } from './retea.route';

@NgModule({
  imports: [SparkMeSharedModule, RouterModule.forChild(reteaRoute)],
  declarations: [ReteaComponent, ReteaDetailComponent, ReteaUpdateComponent, ReteaDeleteDialogComponent],
  entryComponents: [ReteaDeleteDialogComponent],
})
export class SparkMeReteaModule {}
