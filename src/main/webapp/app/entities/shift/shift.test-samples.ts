import dayjs from 'dayjs/esm';

import { RequestStatus } from 'app/entities/enumerations/request-status.model';

import { IShift, NewShift } from './shift.model';

export const sampleWithRequiredData: IShift = {
  id: 77164,
};

export const sampleWithPartialData: IShift = {
  id: 11473,
  shiftType: RequestStatus['REJECTED'],
};

export const sampleWithFullData: IShift = {
  id: 57051,
  shiftDate: dayjs('2023-05-28T00:18'),
  shiftType: RequestStatus['APPROVED'],
};

export const sampleWithNewData: NewShift = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
