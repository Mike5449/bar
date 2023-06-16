import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPrixBoisson, NewPrixBoisson } from '../prix-boisson.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPrixBoisson for edit and NewPrixBoissonFormGroupInput for create.
 */
type PrixBoissonFormGroupInput = IPrixBoisson | PartialWithRequiredKeyOf<NewPrixBoisson>;

type PrixBoissonFormDefaults = Pick<NewPrixBoisson, 'id'>;

type PrixBoissonFormGroupContent = {
  id: FormControl<IPrixBoisson['id'] | NewPrixBoisson['id']>;
  prix: FormControl<IPrixBoisson['prix']>;
  boisson: FormControl<IPrixBoisson['boisson']>;
};

export type PrixBoissonFormGroup = FormGroup<PrixBoissonFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PrixBoissonFormService {
  createPrixBoissonFormGroup(prixBoisson: PrixBoissonFormGroupInput = { id: null }): PrixBoissonFormGroup {
    const prixBoissonRawValue = {
      ...this.getFormDefaults(),
      ...prixBoisson,
    };
    return new FormGroup<PrixBoissonFormGroupContent>({
      id: new FormControl(
        { value: prixBoissonRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      prix: new FormControl(prixBoissonRawValue.prix, {
        validators: [Validators.required],
      }),
      boisson: new FormControl(prixBoissonRawValue.boisson),
    });
  }

  getPrixBoisson(form: PrixBoissonFormGroup): IPrixBoisson | NewPrixBoisson {
    return form.getRawValue() as IPrixBoisson | NewPrixBoisson;
  }

  resetForm(form: PrixBoissonFormGroup, prixBoisson: PrixBoissonFormGroupInput): void {
    const prixBoissonRawValue = { ...this.getFormDefaults(), ...prixBoisson };
    form.reset(
      {
        ...prixBoissonRawValue,
        id: { value: prixBoissonRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PrixBoissonFormDefaults {
    return {
      id: null,
    };
  }
}
