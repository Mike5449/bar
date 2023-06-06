import dayjs from 'dayjs/esm';

import { IAttendance, NewAttendance } from './attendance.model';

export const sampleWithRequiredData: IAttendance = {
  id: 67192,
};

export const sampleWithPartialData: IAttendance = {
  id: 61584,
  isPresent: true,
};

export const sampleWithFullData: IAttendance = {
  id: 38898,
  date: dayjs('2023-05-27T22:46'),
  isPresent: false,
};

export const sampleWithNewData: NewAttendance = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
