import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PrixBoissonComponent } from '../list/prix-boisson.component';
import { PrixBoissonDetailComponent } from '../detail/prix-boisson-detail.component';
import { PrixBoissonUpdateComponent } from '../update/prix-boisson-update.component';
import { PrixBoissonRoutingResolveService } from './prix-boisson-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const prixBoissonRoute: Routes = [
  {
    path: '',
    component: PrixBoissonComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PrixBoissonDetailComponent,
    resolve: {
      prixBoisson: PrixBoissonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PrixBoissonUpdateComponent,
    resolve: {
      prixBoisson: PrixBoissonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PrixBoissonUpdateComponent,
    resolve: {
      prixBoisson: PrixBoissonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(prixBoissonRoute)],
  exports: [RouterModule],
})
export class PrixBoissonRoutingModule {}
