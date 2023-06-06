import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProductPriceComponent } from '../list/product-price.component';
import { ProductPriceDetailComponent } from '../detail/product-price-detail.component';
import { ProductPriceUpdateComponent } from '../update/product-price-update.component';
import { ProductPriceRoutingResolveService } from './product-price-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const productPriceRoute: Routes = [
  {
    path: '',
    component: ProductPriceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductPriceDetailComponent,
    resolve: {
      productPrice: ProductPriceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductPriceUpdateComponent,
    resolve: {
      productPrice: ProductPriceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductPriceUpdateComponent,
    resolve: {
      productPrice: ProductPriceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(productPriceRoute)],
  exports: [RouterModule],
})
export class ProductPriceRoutingModule {}
