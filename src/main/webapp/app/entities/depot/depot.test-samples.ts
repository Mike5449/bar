import { IDepot, NewDepot } from './depot.model';

export const sampleWithRequiredData: IDepot = {
  id: 1234,
  amount: 20102,
};

export const sampleWithPartialData: IDepot = {
  id: 65172,
  amount: 13750,
};

export const sampleWithFullData: IDepot = {
  id: 49006,
  amount: 14434,
  description: 'action-items',
};

export const sampleWithNewData: NewDepot = {
  amount: 30509,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
