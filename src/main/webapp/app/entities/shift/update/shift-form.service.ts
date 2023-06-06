import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IShift, NewShift } from '../shift.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IShift for edit and NewShiftFormGroupInput for create.
 */
type ShiftFormGroupInput = IShift | PartialWithRequiredKeyOf<NewShift>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IShift | NewShift> = Omit<T, 'shiftDate'> & {
  shiftDate?: string | null;
};

type ShiftFormRawValue = FormValueOf<IShift>;

type NewShiftFormRawValue = FormValueOf<NewShift>;

type ShiftFormDefaults = Pick<NewShift, 'id' | 'shiftDate'>;

type ShiftFormGroupContent = {
  id: FormControl<ShiftFormRawValue['id'] | NewShift['id']>;
  shiftDate: FormControl<ShiftFormRawValue['shiftDate']>;
  shiftType: FormControl<ShiftFormRawValue['shiftType']>;
  employee: FormControl<ShiftFormRawValue['employee']>;
};

export type ShiftFormGroup = FormGroup<ShiftFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ShiftFormService {
  createShiftFormGroup(shift: ShiftFormGroupInput = { id: null }): ShiftFormGroup {
    const shiftRawValue = this.convertShiftToShiftRawValue({
      ...this.getFormDefaults(),
      ...shift,
    });
    return new FormGroup<ShiftFormGroupContent>({
      id: new FormControl(
        { value: shiftRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      shiftDate: new FormControl(shiftRawValue.shiftDate),
      shiftType: new FormControl(shiftRawValue.shiftType),
      employee: new FormControl(shiftRawValue.employee),
    });
  }

  getShift(form: ShiftFormGroup): IShift | NewShift {
    return this.convertShiftRawValueToShift(form.getRawValue() as ShiftFormRawValue | NewShiftFormRawValue);
  }

  resetForm(form: ShiftFormGroup, shift: ShiftFormGroupInput): void {
    const shiftRawValue = this.convertShiftToShiftRawValue({ ...this.getFormDefaults(), ...shift });
    form.reset(
      {
        ...shiftRawValue,
        id: { value: shiftRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ShiftFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      shiftDate: currentTime,
    };
  }

  private convertShiftRawValueToShift(rawShift: ShiftFormRawValue | NewShiftFormRawValue): IShift | NewShift {
    return {
      ...rawShift,
      shiftDate: dayjs(rawShift.shiftDate, DATE_TIME_FORMAT),
    };
  }

  private convertShiftToShiftRawValue(
    shift: IShift | (Partial<NewShift> & ShiftFormDefaults)
  ): ShiftFormRawValue | PartialWithRequiredKeyOf<NewShiftFormRawValue> {
    return {
      ...shift,
      shiftDate: shift.shiftDate ? shift.shiftDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
