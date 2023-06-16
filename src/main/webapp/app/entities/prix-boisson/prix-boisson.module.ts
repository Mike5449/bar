import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PrixBoissonComponent } from './list/prix-boisson.component';
import { PrixBoissonDetailComponent } from './detail/prix-boisson-detail.component';
import { PrixBoissonUpdateComponent } from './update/prix-boisson-update.component';
import { PrixBoissonDeleteDialogComponent } from './delete/prix-boisson-delete-dialog.component';
import { PrixBoissonRoutingModule } from './route/prix-boisson-routing.module';

@NgModule({
  imports: [SharedModule, PrixBoissonRoutingModule],
  declarations: [PrixBoissonComponent, PrixBoissonDetailComponent, PrixBoissonUpdateComponent, PrixBoissonDeleteDialogComponent],
})
export class PrixBoissonModule {}
