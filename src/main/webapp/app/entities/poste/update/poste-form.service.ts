import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPoste, NewPoste } from '../poste.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPoste for edit and NewPosteFormGroupInput for create.
 */
type PosteFormGroupInput = IPoste | PartialWithRequiredKeyOf<NewPoste>;

type PosteFormDefaults = Pick<NewPoste, 'id'>;

type PosteFormGroupContent = {
  id: FormControl<IPoste['id'] | NewPoste['id']>;
  posteName: FormControl<IPoste['posteName']>;
};

export type PosteFormGroup = FormGroup<PosteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PosteFormService {
  createPosteFormGroup(poste: PosteFormGroupInput = { id: null }): PosteFormGroup {
    const posteRawValue = {
      ...this.getFormDefaults(),
      ...poste,
    };
    return new FormGroup<PosteFormGroupContent>({
      id: new FormControl(
        { value: posteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      posteName: new FormControl(posteRawValue.posteName),
    });
  }

  getPoste(form: PosteFormGroup): IPoste | NewPoste {
    return form.getRawValue() as IPoste | NewPoste;
  }

  resetForm(form: PosteFormGroup, poste: PosteFormGroupInput): void {
    const posteRawValue = { ...this.getFormDefaults(), ...poste };
    form.reset(
      {
        ...posteRawValue,
        id: { value: posteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PosteFormDefaults {
    return {
      id: null,
    };
  }
}
