import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISalary, NewSalary } from '../salary.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISalary for edit and NewSalaryFormGroupInput for create.
 */
type SalaryFormGroupInput = ISalary | PartialWithRequiredKeyOf<NewSalary>;

type SalaryFormDefaults = Pick<NewSalary, 'id'>;

type SalaryFormGroupContent = {
  id: FormControl<ISalary['id'] | NewSalary['id']>;
  month: FormControl<ISalary['month']>;
  year: FormControl<ISalary['year']>;
  amount: FormControl<ISalary['amount']>;
  employee: FormControl<ISalary['employee']>;
};

export type SalaryFormGroup = FormGroup<SalaryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SalaryFormService {
  createSalaryFormGroup(salary: SalaryFormGroupInput = { id: null }): SalaryFormGroup {
    const salaryRawValue = {
      ...this.getFormDefaults(),
      ...salary,
    };
    return new FormGroup<SalaryFormGroupContent>({
      id: new FormControl(
        { value: salaryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      month: new FormControl(salaryRawValue.month),
      year: new FormControl(salaryRawValue.year),
      amount: new FormControl(salaryRawValue.amount, {
        validators: [Validators.required],
      }),
      employee: new FormControl(salaryRawValue.employee),
    });
  }

  getSalary(form: SalaryFormGroup): ISalary | NewSalary {
    return form.getRawValue() as ISalary | NewSalary;
  }

  resetForm(form: SalaryFormGroup, salary: SalaryFormGroupInput): void {
    const salaryRawValue = { ...this.getFormDefaults(), ...salary };
    form.reset(
      {
        ...salaryRawValue,
        id: { value: salaryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SalaryFormDefaults {
    return {
      id: null,
    };
  }
}
