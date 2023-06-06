import { Categorie } from 'app/entities/enumerations/categorie.model';

import { IBoisson, NewBoisson } from './boisson.model';

export const sampleWithRequiredData: IBoisson = {
  id: 95629,
  name: 'capacitor schemas Handcrafted',
  type: Categorie['ALCOLISEE'],
};

export const sampleWithPartialData: IBoisson = {
  id: 72045,
  name: 'implementation deposit',
  type: Categorie['GASEUSE'],
};

export const sampleWithFullData: IBoisson = {
  id: 78511,
  name: 'infrastructures',
  image: 'de Salad Account',
  type: Categorie['AUTRE'],
};

export const sampleWithNewData: NewBoisson = {
  name: 'European Awesome deposit',
  type: Categorie['AUTRE'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
