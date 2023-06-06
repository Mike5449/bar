import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProductPriceComponent } from './list/product-price.component';
import { ProductPriceDetailComponent } from './detail/product-price-detail.component';
import { ProductPriceUpdateComponent } from './update/product-price-update.component';
import { ProductPriceDeleteDialogComponent } from './delete/product-price-delete-dialog.component';
import { ProductPriceRoutingModule } from './route/product-price-routing.module';

@NgModule({
  imports: [SharedModule, ProductPriceRoutingModule],
  declarations: [ProductPriceComponent, ProductPriceDetailComponent, ProductPriceUpdateComponent, ProductPriceDeleteDialogComponent],
})
export class ProductPriceModule {}
