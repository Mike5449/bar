import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PrixComponent } from './list/prix.component';
import { PrixDetailComponent } from './detail/prix-detail.component';
import { PrixUpdateComponent } from './update/prix-update.component';
import { PrixDeleteDialogComponent } from './delete/prix-delete-dialog.component';
import { PrixRoutingModule } from './route/prix-routing.module';

@NgModule({
  imports: [SharedModule, PrixRoutingModule],
  declarations: [PrixComponent, PrixDetailComponent, PrixUpdateComponent, PrixDeleteDialogComponent],
})
export class PrixModule {}
