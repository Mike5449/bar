import { IPoste, NewPoste } from './poste.model';

export const sampleWithRequiredData: IPoste = {
  id: 61403,
};

export const sampleWithPartialData: IPoste = {
  id: 73918,
};

export const sampleWithFullData: IPoste = {
  id: 92675,
  posteName: 'la implement Champagne-Ardenne',
};

export const sampleWithNewData: NewPoste = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
