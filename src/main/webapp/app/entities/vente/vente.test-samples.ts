import { StatusVente } from 'app/entities/enumerations/status-vente.model';

import { IVente, NewVente } from './vente.model';

export const sampleWithRequiredData: IVente = {
  id: 96992,
  quantite: 52190,
};

export const sampleWithPartialData: IVente = {
  id: 81248,
  quantite: 82727,
  status: StatusVente['NEW'],
};

export const sampleWithFullData: IVente = {
  id: 72748,
  quantite: 58597,
  status: StatusVente['ANNULER'],
};

export const sampleWithNewData: NewVente = {
  quantite: 37581,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
