import { StatusCaisse } from 'app/entities/enumerations/status-caisse.model';

import { ICompteCaisse, NewCompteCaisse } from './compte-caisse.model';

export const sampleWithRequiredData: ICompteCaisse = {
  id: 94288,
};

export const sampleWithPartialData: ICompteCaisse = {
  id: 66331,
  cancel: 67984,
  cash: 53006,
  balance: 79103,
  aVerser: 19108,
  status: StatusCaisse['ACTIVE'],
  name: '1080p Electronics',
};

export const sampleWithFullData: ICompteCaisse = {
  id: 52478,
  injection: 32797,
  sale: 12283,
  cancel: 97605,
  cash: 44930,
  pret: 93440,
  balance: 4041,
  aVerser: 83334,
  status: StatusCaisse['INACTIVE'],
  name: 'Assistant Administrateur Steel',
};

export const sampleWithNewData: NewCompteCaisse = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
