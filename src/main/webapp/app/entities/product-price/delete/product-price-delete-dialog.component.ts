import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProductPrice } from '../product-price.model';
import { ProductPriceService } from '../service/product-price.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './product-price-delete-dialog.component.html',
})
export class ProductPriceDeleteDialogComponent {
  productPrice?: IProductPrice;

  constructor(protected productPriceService: ProductPriceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productPriceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
