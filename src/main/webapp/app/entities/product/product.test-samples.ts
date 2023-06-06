import { Categorie } from 'app/entities/enumerations/categorie.model';
import { Section } from 'app/entities/enumerations/section.model';

import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 77672,
  name: 'Customer-focused',
  type: Categorie['AUTRE'],
  section: Section['PISCINE'],
};

export const sampleWithPartialData: IProduct = {
  id: 94362,
  name: 'SSL',
  type: Categorie['ALCOLISEE'],
  section: Section['HOTEL'],
};

export const sampleWithFullData: IProduct = {
  id: 49054,
  name: 'deliver',
  image: 'Administrateur Health',
  type: Categorie['AUTRE'],
  section: Section['RESTAURANT'],
};

export const sampleWithNewData: NewProduct = {
  name: 'neural Automotive c',
  type: Categorie['ALCOLISEE'],
  section: Section['BAR'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
