import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SalaryComponent } from '../list/salary.component';
import { SalaryDetailComponent } from '../detail/salary-detail.component';
import { SalaryUpdateComponent } from '../update/salary-update.component';
import { SalaryRoutingResolveService } from './salary-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const salaryRoute: Routes = [
  {
    path: '',
    component: SalaryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SalaryDetailComponent,
    resolve: {
      salary: SalaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SalaryUpdateComponent,
    resolve: {
      salary: SalaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SalaryUpdateComponent,
    resolve: {
      salary: SalaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(salaryRoute)],
  exports: [RouterModule],
})
export class SalaryRoutingModule {}
