import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IVente, NewVente } from '../vente.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVente for edit and NewVenteFormGroupInput for create.
 */
type VenteFormGroupInput = IVente | PartialWithRequiredKeyOf<NewVente>;

type VenteFormDefaults = Pick<NewVente, 'id'>;

type VenteFormGroupContent = {
  id: FormControl<IVente['id'] | NewVente['id']>;
  quantite: FormControl<IVente['quantite']>;
  status: FormControl<IVente['status']>;
  employee: FormControl<IVente['employee']>;
  client: FormControl<IVente['client']>;
  depot: FormControl<IVente['depot']>;
  boisson: FormControl<IVente['boisson']>;
};

export type VenteFormGroup = FormGroup<VenteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VenteFormService {
  createVenteFormGroup(vente: VenteFormGroupInput = { id: null }): VenteFormGroup {
    const venteRawValue = {
      ...this.getFormDefaults(),
      ...vente,
    };
    return new FormGroup<VenteFormGroupContent>({
      id: new FormControl(
        { value: venteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      quantite: new FormControl(venteRawValue.quantite, {
        validators: [Validators.required],
      }),
      status: new FormControl(venteRawValue.status),
      employee: new FormControl(venteRawValue.employee),
      client: new FormControl(venteRawValue.client),
      depot: new FormControl(venteRawValue.depot),
      boisson: new FormControl(venteRawValue.boisson),
    });
  }

  getVente(form: VenteFormGroup): IVente | NewVente {
    return form.getRawValue() as IVente | NewVente;
  }

  resetForm(form: VenteFormGroup, vente: VenteFormGroupInput): void {
    const venteRawValue = { ...this.getFormDefaults(), ...vente };
    form.reset(
      {
        ...venteRawValue,
        id: { value: venteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): VenteFormDefaults {
    return {
      id: null,
    };
  }
}
