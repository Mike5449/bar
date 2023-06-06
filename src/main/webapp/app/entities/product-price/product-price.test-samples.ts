import { StatusPrice } from 'app/entities/enumerations/status-price.model';

import { IProductPrice, NewProductPrice } from './product-price.model';

export const sampleWithRequiredData: IProductPrice = {
  id: 97826,
  price: 17384,
};

export const sampleWithPartialData: IProductPrice = {
  id: 85060,
  price: 84714,
};

export const sampleWithFullData: IProductPrice = {
  id: 17362,
  price: 85380,
  status: StatusPrice['CURRENT'],
};

export const sampleWithNewData: NewProductPrice = {
  price: 39648,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
