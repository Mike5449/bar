import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILeaveRequest, NewLeaveRequest } from '../leave-request.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILeaveRequest for edit and NewLeaveRequestFormGroupInput for create.
 */
type LeaveRequestFormGroupInput = ILeaveRequest | PartialWithRequiredKeyOf<NewLeaveRequest>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ILeaveRequest | NewLeaveRequest> = Omit<T, 'startDate' | 'enDate'> & {
  startDate?: string | null;
  enDate?: string | null;
};

type LeaveRequestFormRawValue = FormValueOf<ILeaveRequest>;

type NewLeaveRequestFormRawValue = FormValueOf<NewLeaveRequest>;

type LeaveRequestFormDefaults = Pick<NewLeaveRequest, 'id' | 'startDate' | 'enDate'>;

type LeaveRequestFormGroupContent = {
  id: FormControl<LeaveRequestFormRawValue['id'] | NewLeaveRequest['id']>;
  startDate: FormControl<LeaveRequestFormRawValue['startDate']>;
  enDate: FormControl<LeaveRequestFormRawValue['enDate']>;
  requestStatus: FormControl<LeaveRequestFormRawValue['requestStatus']>;
  employee: FormControl<LeaveRequestFormRawValue['employee']>;
};

export type LeaveRequestFormGroup = FormGroup<LeaveRequestFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LeaveRequestFormService {
  createLeaveRequestFormGroup(leaveRequest: LeaveRequestFormGroupInput = { id: null }): LeaveRequestFormGroup {
    const leaveRequestRawValue = this.convertLeaveRequestToLeaveRequestRawValue({
      ...this.getFormDefaults(),
      ...leaveRequest,
    });
    return new FormGroup<LeaveRequestFormGroupContent>({
      id: new FormControl(
        { value: leaveRequestRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      startDate: new FormControl(leaveRequestRawValue.startDate, {
        validators: [Validators.required],
      }),
      enDate: new FormControl(leaveRequestRawValue.enDate, {
        validators: [Validators.required],
      }),
      requestStatus: new FormControl(leaveRequestRawValue.requestStatus),
      employee: new FormControl(leaveRequestRawValue.employee),
    });
  }

  getLeaveRequest(form: LeaveRequestFormGroup): ILeaveRequest | NewLeaveRequest {
    return this.convertLeaveRequestRawValueToLeaveRequest(form.getRawValue() as LeaveRequestFormRawValue | NewLeaveRequestFormRawValue);
  }

  resetForm(form: LeaveRequestFormGroup, leaveRequest: LeaveRequestFormGroupInput): void {
    const leaveRequestRawValue = this.convertLeaveRequestToLeaveRequestRawValue({ ...this.getFormDefaults(), ...leaveRequest });
    form.reset(
      {
        ...leaveRequestRawValue,
        id: { value: leaveRequestRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): LeaveRequestFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startDate: currentTime,
      enDate: currentTime,
    };
  }

  private convertLeaveRequestRawValueToLeaveRequest(
    rawLeaveRequest: LeaveRequestFormRawValue | NewLeaveRequestFormRawValue
  ): ILeaveRequest | NewLeaveRequest {
    return {
      ...rawLeaveRequest,
      startDate: dayjs(rawLeaveRequest.startDate, DATE_TIME_FORMAT),
      enDate: dayjs(rawLeaveRequest.enDate, DATE_TIME_FORMAT),
    };
  }

  private convertLeaveRequestToLeaveRequestRawValue(
    leaveRequest: ILeaveRequest | (Partial<NewLeaveRequest> & LeaveRequestFormDefaults)
  ): LeaveRequestFormRawValue | PartialWithRequiredKeyOf<NewLeaveRequestFormRawValue> {
    return {
      ...leaveRequest,
      startDate: leaveRequest.startDate ? leaveRequest.startDate.format(DATE_TIME_FORMAT) : undefined,
      enDate: leaveRequest.enDate ? leaveRequest.enDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
