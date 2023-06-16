import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ProductPriceFormService, ProductPriceFormGroup } from './product-price-form.service';
import { IProductPrice } from '../product-price.model';
import { ProductPriceService } from '../service/product-price.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { StatusPrice } from 'app/entities/enumerations/status-price.model';

@Component({
  selector: 'jhi-product-price-update',
  templateUrl: './product-price-update.component.html',
})
export class ProductPriceUpdateComponent implements OnInit {
  isSaving = false;
  productPrice: IProductPrice | null = null;
  statusPriceValues = Object.keys(StatusPrice);

  productsSharedCollection: IProduct[] = [];

  editForm: ProductPriceFormGroup = this.productPriceFormService.createProductPriceFormGroup();

  constructor(
    protected productPriceService: ProductPriceService,
    protected productPriceFormService: ProductPriceFormService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productPrice }) => {
      this.productPrice = productPrice;
      if (productPrice) {
        this.updateForm(productPrice);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productPrice = this.productPriceFormService.getProductPrice(this.editForm);
    if (productPrice.id !== null) {
      this.subscribeToSaveResponse(this.productPriceService.update(productPrice));
    } else {
      this.subscribeToSaveResponse(this.productPriceService.create(productPrice));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductPrice>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(productPrice: IProductPrice): void {
    this.productPrice = productPrice;
    this.productPriceFormService.resetForm(this.editForm, productPrice);

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      productPrice.product
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.productPrice?.product))
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }
}
