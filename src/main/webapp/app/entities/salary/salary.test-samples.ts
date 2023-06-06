import { ISalary, NewSalary } from './salary.model';

export const sampleWithRequiredData: ISalary = {
  id: 66226,
  amount: 13095,
};

export const sampleWithPartialData: ISalary = {
  id: 94402,
  month: 77953,
  amount: 19899,
};

export const sampleWithFullData: ISalary = {
  id: 79312,
  month: 95040,
  year: 4877,
  amount: 56286,
};

export const sampleWithNewData: NewSalary = {
  amount: 33180,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
