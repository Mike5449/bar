import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBoisson, NewBoisson } from '../boisson.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBoisson for edit and NewBoissonFormGroupInput for create.
 */
type BoissonFormGroupInput = IBoisson | PartialWithRequiredKeyOf<NewBoisson>;

type BoissonFormDefaults = Pick<NewBoisson, 'id'>;

type BoissonFormGroupContent = {
  id: FormControl<IBoisson['id'] | NewBoisson['id']>;
  name: FormControl<IBoisson['name']>;
  image: FormControl<IBoisson['image']>;
  type: FormControl<IBoisson['type']>;
};

export type BoissonFormGroup = FormGroup<BoissonFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BoissonFormService {
  createBoissonFormGroup(boisson: BoissonFormGroupInput = { id: null }): BoissonFormGroup {
    const boissonRawValue = {
      ...this.getFormDefaults(),
      ...boisson,
    };
    return new FormGroup<BoissonFormGroupContent>({
      id: new FormControl(
        { value: boissonRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(boissonRawValue.name, {
        validators: [Validators.required],
      }),
      image: new FormControl(boissonRawValue.image),
      type: new FormControl(boissonRawValue.type, {
        validators: [Validators.required],
      }),
    });
  }

  getBoisson(form: BoissonFormGroup): IBoisson | NewBoisson {
    return form.getRawValue() as IBoisson | NewBoisson;
  }

  resetForm(form: BoissonFormGroup, boisson: BoissonFormGroupInput): void {
    const boissonRawValue = { ...this.getFormDefaults(), ...boisson };
    form.reset(
      {
        ...boissonRawValue,
        id: { value: boissonRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BoissonFormDefaults {
    return {
      id: null,
    };
  }
}
