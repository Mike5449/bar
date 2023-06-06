import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProductPrice, NewProductPrice } from '../product-price.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductPrice for edit and NewProductPriceFormGroupInput for create.
 */
type ProductPriceFormGroupInput = IProductPrice | PartialWithRequiredKeyOf<NewProductPrice>;

type ProductPriceFormDefaults = Pick<NewProductPrice, 'id'>;

type ProductPriceFormGroupContent = {
  id: FormControl<IProductPrice['id'] | NewProductPrice['id']>;
  price: FormControl<IProductPrice['price']>;
  status: FormControl<IProductPrice['status']>;
  produit: FormControl<IProductPrice['produit']>;
};

export type ProductPriceFormGroup = FormGroup<ProductPriceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductPriceFormService {
  createProductPriceFormGroup(productPrice: ProductPriceFormGroupInput = { id: null }): ProductPriceFormGroup {
    const productPriceRawValue = {
      ...this.getFormDefaults(),
      ...productPrice,
    };
    return new FormGroup<ProductPriceFormGroupContent>({
      id: new FormControl(
        { value: productPriceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      price: new FormControl(productPriceRawValue.price, {
        validators: [Validators.required],
      }),
      status: new FormControl(productPriceRawValue.status),
      produit: new FormControl(productPriceRawValue.produit),
    });
  }

  getProductPrice(form: ProductPriceFormGroup): IProductPrice | NewProductPrice {
    return form.getRawValue() as IProductPrice | NewProductPrice;
  }

  resetForm(form: ProductPriceFormGroup, productPrice: ProductPriceFormGroupInput): void {
    const productPriceRawValue = { ...this.getFormDefaults(), ...productPrice };
    form.reset(
      {
        ...productPriceRawValue,
        id: { value: productPriceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProductPriceFormDefaults {
    return {
      id: null,
    };
  }
}
