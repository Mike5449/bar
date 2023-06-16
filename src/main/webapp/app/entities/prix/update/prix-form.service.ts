import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPrix, NewPrix } from '../prix.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPrix for edit and NewPrixFormGroupInput for create.
 */
type PrixFormGroupInput = IPrix | PartialWithRequiredKeyOf<NewPrix>;

type PrixFormDefaults = Pick<NewPrix, 'id'>;

type PrixFormGroupContent = {
  id: FormControl<IPrix['id'] | NewPrix['id']>;
  prix: FormControl<IPrix['prix']>;
  date: FormControl<IPrix['date']>;
  boisson: FormControl<IPrix['boisson']>;
};

export type PrixFormGroup = FormGroup<PrixFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PrixFormService {
  createPrixFormGroup(prix: PrixFormGroupInput = { id: null }): PrixFormGroup {
    const prixRawValue = {
      ...this.getFormDefaults(),
      ...prix,
    };
    return new FormGroup<PrixFormGroupContent>({
      id: new FormControl(
        { value: prixRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      prix: new FormControl(prixRawValue.prix, {
        validators: [Validators.required],
      }),
      date: new FormControl(prixRawValue.date, {
        validators: [Validators.required],
      }),
      boisson: new FormControl(prixRawValue.boisson),
    });
  }

  getPrix(form: PrixFormGroup): IPrix | NewPrix {
    return form.getRawValue() as IPrix | NewPrix;
  }

  resetForm(form: PrixFormGroup, prix: PrixFormGroupInput): void {
    const prixRawValue = { ...this.getFormDefaults(), ...prix };
    form.reset(
      {
        ...prixRawValue,
        id: { value: prixRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PrixFormDefaults {
    return {
      id: null,
    };
  }
}
