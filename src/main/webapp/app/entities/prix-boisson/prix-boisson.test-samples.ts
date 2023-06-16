import { IPrixBoisson, NewPrixBoisson } from './prix-boisson.model';

export const sampleWithRequiredData: IPrixBoisson = {
  id: 27313,
  prix: 75426,
};

export const sampleWithPartialData: IPrixBoisson = {
  id: 24898,
  prix: 16440,
};

export const sampleWithFullData: IPrixBoisson = {
  id: 17291,
  prix: 24067,
};

export const sampleWithNewData: NewPrixBoisson = {
  prix: 21721,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
