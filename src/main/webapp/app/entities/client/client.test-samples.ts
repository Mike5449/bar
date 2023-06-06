import { IClient, NewClient } from './client.model';

export const sampleWithRequiredData: IClient = {
  id: 71655,
  name: 'hacking',
};

export const sampleWithPartialData: IClient = {
  id: 91535,
  name: 'Salad Concrete a',
};

export const sampleWithFullData: IClient = {
  id: 51037,
  name: 'programming homogeneous',
};

export const sampleWithNewData: NewClient = {
  name: 'Bedfordshire',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
