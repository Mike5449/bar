import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SalaryComponent } from './list/salary.component';
import { SalaryDetailComponent } from './detail/salary-detail.component';
import { SalaryUpdateComponent } from './update/salary-update.component';
import { SalaryDeleteDialogComponent } from './delete/salary-delete-dialog.component';
import { SalaryRoutingModule } from './route/salary-routing.module';

@NgModule({
  imports: [SharedModule, SalaryRoutingModule],
  declarations: [SalaryComponent, SalaryDetailComponent, SalaryUpdateComponent, SalaryDeleteDialogComponent],
})
export class SalaryModule {}
