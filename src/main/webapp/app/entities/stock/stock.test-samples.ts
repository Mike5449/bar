import { IStock, NewStock } from './stock.model';

export const sampleWithRequiredData: IStock = {
  id: 51685,
  quantity: 12476,
  buyPrice: 68529,
  quantityMinimal: 64815,
};

export const sampleWithPartialData: IStock = {
  id: 84173,
  quantity: 96686,
  buyPrice: 31511,
  quantityMinimal: 62180,
};

export const sampleWithFullData: IStock = {
  id: 42479,
  quantity: 2388,
  buyPrice: 54546,
  quantityMinimal: 72915,
};

export const sampleWithNewData: NewStock = {
  quantity: 70015,
  buyPrice: 79917,
  quantityMinimal: 41782,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
