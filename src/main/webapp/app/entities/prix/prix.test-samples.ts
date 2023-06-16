import dayjs from 'dayjs/esm';

import { IPrix, NewPrix } from './prix.model';

export const sampleWithRequiredData: IPrix = {
  id: 82653,
  prix: 67473,
  date: dayjs('2023-05-29'),
};

export const sampleWithPartialData: IPrix = {
  id: 77572,
  prix: 4713,
  date: dayjs('2023-05-29'),
};

export const sampleWithFullData: IPrix = {
  id: 41849,
  prix: 72781,
  date: dayjs('2023-05-29'),
};

export const sampleWithNewData: NewPrix = {
  prix: 29910,
  date: dayjs('2023-05-28'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
