import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LeaveRequestComponent } from './list/leave-request.component';
import { LeaveRequestDetailComponent } from './detail/leave-request-detail.component';
import { LeaveRequestUpdateComponent } from './update/leave-request-update.component';
import { LeaveRequestDeleteDialogComponent } from './delete/leave-request-delete-dialog.component';
import { LeaveRequestRoutingModule } from './route/leave-request-routing.module';

@NgModule({
  imports: [SharedModule, LeaveRequestRoutingModule],
  declarations: [LeaveRequestComponent, LeaveRequestDetailComponent, LeaveRequestUpdateComponent, LeaveRequestDeleteDialogComponent],
})
export class LeaveRequestModule {}
