import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PrixComponent } from '../list/prix.component';
import { PrixDetailComponent } from '../detail/prix-detail.component';
import { PrixUpdateComponent } from '../update/prix-update.component';
import { PrixRoutingResolveService } from './prix-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const prixRoute: Routes = [
  {
    path: '',
    component: PrixComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PrixDetailComponent,
    resolve: {
      prix: PrixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PrixUpdateComponent,
    resolve: {
      prix: PrixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PrixUpdateComponent,
    resolve: {
      prix: PrixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(prixRoute)],
  exports: [RouterModule],
})
export class PrixRoutingModule {}
