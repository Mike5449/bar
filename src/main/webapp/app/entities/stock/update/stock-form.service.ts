import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IStock, NewStock } from '../stock.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStock for edit and NewStockFormGroupInput for create.
 */
type StockFormGroupInput = IStock | PartialWithRequiredKeyOf<NewStock>;

type StockFormDefaults = Pick<NewStock, 'id'>;

type StockFormGroupContent = {
  id: FormControl<IStock['id'] | NewStock['id']>;
  quantity: FormControl<IStock['quantity']>;
  buyPrice: FormControl<IStock['buyPrice']>;
  quantityMinimal: FormControl<IStock['quantityMinimal']>;
  product: FormControl<IStock['product']>;
};

export type StockFormGroup = FormGroup<StockFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StockFormService {
  createStockFormGroup(stock: StockFormGroupInput = { id: null }): StockFormGroup {
    const stockRawValue = {
      ...this.getFormDefaults(),
      ...stock,
    };
    return new FormGroup<StockFormGroupContent>({
      id: new FormControl(
        { value: stockRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      quantity: new FormControl(stockRawValue.quantity, {
        validators: [Validators.required],
      }),
      buyPrice: new FormControl(stockRawValue.buyPrice, {
        validators: [Validators.required],
      }),
      quantityMinimal: new FormControl(stockRawValue.quantityMinimal, {
        validators: [Validators.required],
      }),
      product: new FormControl(stockRawValue.product),
    });
  }

  getStock(form: StockFormGroup): IStock | NewStock {
    return form.getRawValue() as IStock | NewStock;
  }

  resetForm(form: StockFormGroup, stock: StockFormGroupInput): void {
    const stockRawValue = { ...this.getFormDefaults(), ...stock };
    form.reset(
      {
        ...stockRawValue,
        id: { value: stockRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): StockFormDefaults {
    return {
      id: null,
    };
  }
}
