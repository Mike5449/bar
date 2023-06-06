import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeaveRequestComponent } from '../list/leave-request.component';
import { LeaveRequestDetailComponent } from '../detail/leave-request-detail.component';
import { LeaveRequestUpdateComponent } from '../update/leave-request-update.component';
import { LeaveRequestRoutingResolveService } from './leave-request-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const leaveRequestRoute: Routes = [
  {
    path: '',
    component: LeaveRequestComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaveRequestDetailComponent,
    resolve: {
      leaveRequest: LeaveRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeaveRequestUpdateComponent,
    resolve: {
      leaveRequest: LeaveRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeaveRequestUpdateComponent,
    resolve: {
      leaveRequest: LeaveRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leaveRequestRoute)],
  exports: [RouterModule],
})
export class LeaveRequestRoutingModule {}
