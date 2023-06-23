import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICompteCaisse, NewCompteCaisse } from '../compte-caisse.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICompteCaisse for edit and NewCompteCaisseFormGroupInput for create.
 */
type CompteCaisseFormGroupInput = ICompteCaisse | PartialWithRequiredKeyOf<NewCompteCaisse>;

type CompteCaisseFormDefaults = Pick<NewCompteCaisse, 'id'>;

type CompteCaisseFormGroupContent = {
  id: FormControl<ICompteCaisse['id'] | NewCompteCaisse['id']>;
  injection: FormControl<ICompteCaisse['injection']>;
  sale: FormControl<ICompteCaisse['sale']>;
  cancel: FormControl<ICompteCaisse['cancel']>;
  cash: FormControl<ICompteCaisse['cash']>;
  pret: FormControl<ICompteCaisse['pret']>;
  balance: FormControl<ICompteCaisse['balance']>;
  aVerser: FormControl<ICompteCaisse['aVerser']>;
  status: FormControl<ICompteCaisse['status']>;
  name: FormControl<ICompteCaisse['name']>;
};

export type CompteCaisseFormGroup = FormGroup<CompteCaisseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CompteCaisseFormService {
  createCompteCaisseFormGroup(compteCaisse: CompteCaisseFormGroupInput = { id: null }): CompteCaisseFormGroup {
    const compteCaisseRawValue = {
      ...this.getFormDefaults(),
      ...compteCaisse,
    };
    return new FormGroup<CompteCaisseFormGroupContent>({
      id: new FormControl(
        { value: compteCaisseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      injection: new FormControl(compteCaisseRawValue.injection),
      sale: new FormControl(compteCaisseRawValue.sale),
      cancel: new FormControl(compteCaisseRawValue.cancel),
      cash: new FormControl(compteCaisseRawValue.cash),
      pret: new FormControl(compteCaisseRawValue.pret),
      balance: new FormControl(compteCaisseRawValue.balance),
      aVerser: new FormControl(compteCaisseRawValue.aVerser),
      status: new FormControl(compteCaisseRawValue.status),
      name: new FormControl(compteCaisseRawValue.name),
    });
  }

  getCompteCaisse(form: CompteCaisseFormGroup): ICompteCaisse | NewCompteCaisse {
    return form.getRawValue() as ICompteCaisse | NewCompteCaisse;
  }

  resetForm(form: CompteCaisseFormGroup, compteCaisse: CompteCaisseFormGroupInput): void {
    const compteCaisseRawValue = { ...this.getFormDefaults(), ...compteCaisse };
    form.reset(
      {
        ...compteCaisseRawValue,
        id: { value: compteCaisseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CompteCaisseFormDefaults {
    return {
      id: null,
    };
  }
}
