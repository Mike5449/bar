import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEmployee, NewEmployee } from '../employee.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmployee for edit and NewEmployeeFormGroupInput for create.
 */
type EmployeeFormGroupInput = IEmployee | PartialWithRequiredKeyOf<NewEmployee>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEmployee | NewEmployee> = Omit<T, 'dateOfBirth' | 'hireDate' | 'terminationDate'> & {
  dateOfBirth?: string | null;
  hireDate?: string | null;
  terminationDate?: string | null;
};

type EmployeeFormRawValue = FormValueOf<IEmployee>;

type NewEmployeeFormRawValue = FormValueOf<NewEmployee>;

type EmployeeFormDefaults = Pick<NewEmployee, 'id' | 'dateOfBirth' | 'hireDate' | 'terminationDate'>;

type EmployeeFormGroupContent = {
  id: FormControl<EmployeeFormRawValue['id'] | NewEmployee['id']>;
  firstName: FormControl<EmployeeFormRawValue['firstName']>;
  login: FormControl<EmployeeFormRawValue['login']>;
  password: FormControl<EmployeeFormRawValue['password']>;
  confirm: FormControl<EmployeeFormRawValue['confirm']>;
  lastName: FormControl<EmployeeFormRawValue['lastName']>;
  gender: FormControl<EmployeeFormRawValue['gender']>;
  dateOfBirth: FormControl<EmployeeFormRawValue['dateOfBirth']>;
  contactNumber: FormControl<EmployeeFormRawValue['contactNumber']>;
  email: FormControl<EmployeeFormRawValue['email']>;
  nifCin: FormControl<EmployeeFormRawValue['nifCin']>;
  address: FormControl<EmployeeFormRawValue['address']>;
  baseSalary: FormControl<EmployeeFormRawValue['baseSalary']>;
  commissionRate: FormControl<EmployeeFormRawValue['commissionRate']>;
  salary: FormControl<EmployeeFormRawValue['salary']>;
  hireDate: FormControl<EmployeeFormRawValue['hireDate']>;
  terminationDate: FormControl<EmployeeFormRawValue['terminationDate']>;
  job: FormControl<EmployeeFormRawValue['job']>;
};

export type EmployeeFormGroup = FormGroup<EmployeeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmployeeFormService {
  createEmployeeFormGroup(employee: EmployeeFormGroupInput = { id: null }): EmployeeFormGroup {
    const employeeRawValue = this.convertEmployeeToEmployeeRawValue({
      ...this.getFormDefaults(),
      ...employee,
    });
    return new FormGroup<EmployeeFormGroupContent>({
      id: new FormControl(
        { value: employeeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      firstName: new FormControl(employeeRawValue.firstName, {
        validators: [Validators.required],
      }),
      login: new FormControl(employeeRawValue.login, {
        validators: [Validators.required],
      }),
      password: new FormControl(employeeRawValue.password, {
        validators: [Validators.required],
      }),
      confirm: new FormControl(employeeRawValue.confirm, {
        validators: [Validators.required],
      }),
      lastName: new FormControl(employeeRawValue.lastName, {
        validators: [Validators.required],
      }),
      gender: new FormControl(employeeRawValue.gender, {
        validators: [Validators.required],
      }),
      dateOfBirth: new FormControl(employeeRawValue.dateOfBirth),
      contactNumber: new FormControl(employeeRawValue.contactNumber, {
        validators: [Validators.required],
      }),
      email: new FormControl(employeeRawValue.email),
      nifCin: new FormControl(employeeRawValue.nifCin),
      address: new FormControl(employeeRawValue.address),
      baseSalary: new FormControl(employeeRawValue.baseSalary),
      commissionRate: new FormControl(employeeRawValue.commissionRate),
      salary: new FormControl(employeeRawValue.salary),
      hireDate: new FormControl(employeeRawValue.hireDate),
      terminationDate: new FormControl(employeeRawValue.terminationDate),
      job: new FormControl(employeeRawValue.job),
    });
  }

  getEmployee(form: EmployeeFormGroup): IEmployee | NewEmployee {
    return this.convertEmployeeRawValueToEmployee(form.getRawValue() as EmployeeFormRawValue | NewEmployeeFormRawValue);
  }

  resetForm(form: EmployeeFormGroup, employee: EmployeeFormGroupInput): void {
    const employeeRawValue = this.convertEmployeeToEmployeeRawValue({ ...this.getFormDefaults(), ...employee });
    form.reset(
      {
        ...employeeRawValue,
        id: { value: employeeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EmployeeFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateOfBirth: currentTime,
      hireDate: currentTime,
      terminationDate: currentTime,
    };
  }

  private convertEmployeeRawValueToEmployee(rawEmployee: EmployeeFormRawValue | NewEmployeeFormRawValue): IEmployee | NewEmployee {
    return {
      ...rawEmployee,
      dateOfBirth: dayjs(rawEmployee.dateOfBirth, DATE_TIME_FORMAT),
      hireDate: dayjs(rawEmployee.hireDate, DATE_TIME_FORMAT),
      terminationDate: dayjs(rawEmployee.terminationDate, DATE_TIME_FORMAT),
    };
  }

  private convertEmployeeToEmployeeRawValue(
    employee: IEmployee | (Partial<NewEmployee> & EmployeeFormDefaults)
  ): EmployeeFormRawValue | PartialWithRequiredKeyOf<NewEmployeeFormRawValue> {
    return {
      ...employee,
      dateOfBirth: employee.dateOfBirth ? employee.dateOfBirth.format(DATE_TIME_FORMAT) : undefined,
      hireDate: employee.hireDate ? employee.hireDate.format(DATE_TIME_FORMAT) : undefined,
      terminationDate: employee.terminationDate ? employee.terminationDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
