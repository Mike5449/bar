import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CompteCaisseComponent } from './list/compte-caisse.component';
import { CompteCaisseDetailComponent } from './detail/compte-caisse-detail.component';
import { CompteCaisseUpdateComponent } from './update/compte-caisse-update.component';
import { CompteCaisseDeleteDialogComponent } from './delete/compte-caisse-delete-dialog.component';
import { CompteCaisseRoutingModule } from './route/compte-caisse-routing.module';

@NgModule({
  imports: [SharedModule, CompteCaisseRoutingModule],
  declarations: [CompteCaisseComponent, CompteCaisseDetailComponent, CompteCaisseUpdateComponent, CompteCaisseDeleteDialogComponent],
})
export class CompteCaisseModule {}
