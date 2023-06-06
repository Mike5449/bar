import dayjs from 'dayjs/esm';

import { RequestStatus } from 'app/entities/enumerations/request-status.model';

import { ILeaveRequest, NewLeaveRequest } from './leave-request.model';

export const sampleWithRequiredData: ILeaveRequest = {
  id: 25883,
  startDate: dayjs('2023-05-27T01:49'),
  enDate: dayjs('2023-05-27T03:42'),
};

export const sampleWithPartialData: ILeaveRequest = {
  id: 39809,
  startDate: dayjs('2023-05-27T02:09'),
  enDate: dayjs('2023-05-27T06:57'),
  requestStatus: RequestStatus['PENDING'],
};

export const sampleWithFullData: ILeaveRequest = {
  id: 6833,
  startDate: dayjs('2023-05-27T08:13'),
  enDate: dayjs('2023-05-27T20:56'),
  requestStatus: RequestStatus['PENDING'],
};

export const sampleWithNewData: NewLeaveRequest = {
  startDate: dayjs('2023-05-27T05:23'),
  enDate: dayjs('2023-05-27T21:39'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
