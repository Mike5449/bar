import { StatusVente } from 'app/entities/enumerations/status-vente.model';

import { ISale, NewSale } from './sale.model';

export const sampleWithRequiredData: ISale = {
  id: 89826,
  quantity: 19697,
};

export const sampleWithPartialData: ISale = {
  id: 2520,
  quantity: 78708,
  unitPrice: 70368,
  status: StatusVente['VALIDATE'],
};

export const sampleWithFullData: ISale = {
  id: 79176,
  quantity: 63174,
  unitPrice: 48529,
  amountTotal: 4615,
  status: StatusVente['CANCEL'],
};

export const sampleWithNewData: NewSale = {
  quantity: 7882,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
