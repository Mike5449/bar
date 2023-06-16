import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDepot, NewDepot } from '../depot.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDepot for edit and NewDepotFormGroupInput for create.
 */
type DepotFormGroupInput = IDepot | PartialWithRequiredKeyOf<NewDepot>;

type DepotFormDefaults = Pick<NewDepot, 'id'>;

type DepotFormGroupContent = {
  id: FormControl<IDepot['id'] | NewDepot['id']>;
  amount: FormControl<IDepot['amount']>;
  description: FormControl<IDepot['description']>;
  employee: FormControl<IDepot['employee']>;
  client: FormControl<IDepot['client']>;
};

export type DepotFormGroup = FormGroup<DepotFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DepotFormService {
  createDepotFormGroup(depot: DepotFormGroupInput = { id: null }): DepotFormGroup {
    const depotRawValue = {
      ...this.getFormDefaults(),
      ...depot,
    };
    return new FormGroup<DepotFormGroupContent>({
      id: new FormControl(
        { value: depotRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      amount: new FormControl(depotRawValue.amount, {
        validators: [Validators.required],
      }),
      description: new FormControl(depotRawValue.description),
      employee: new FormControl(depotRawValue.employee),
      client: new FormControl(depotRawValue.client),
    });
  }

  getDepot(form: DepotFormGroup): IDepot | NewDepot {
    return form.getRawValue() as IDepot | NewDepot;
  }

  resetForm(form: DepotFormGroup, depot: DepotFormGroupInput): void {
    const depotRawValue = { ...this.getFormDefaults(), ...depot };
    form.reset(
      {
        ...depotRawValue,
        id: { value: depotRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DepotFormDefaults {
    return {
      id: null,
    };
  }
}
