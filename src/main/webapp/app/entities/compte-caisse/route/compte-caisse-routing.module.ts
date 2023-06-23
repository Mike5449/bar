import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CompteCaisseComponent } from '../list/compte-caisse.component';
import { CompteCaisseDetailComponent } from '../detail/compte-caisse-detail.component';
import { CompteCaisseUpdateComponent } from '../update/compte-caisse-update.component';
import { CompteCaisseRoutingResolveService } from './compte-caisse-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const compteCaisseRoute: Routes = [
  {
    path: '',
    component: CompteCaisseComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CompteCaisseDetailComponent,
    resolve: {
      compteCaisse: CompteCaisseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CompteCaisseUpdateComponent,
    resolve: {
      compteCaisse: CompteCaisseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CompteCaisseUpdateComponent,
    resolve: {
      compteCaisse: CompteCaisseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(compteCaisseRoute)],
  exports: [RouterModule],
})
export class CompteCaisseRoutingModule {}
