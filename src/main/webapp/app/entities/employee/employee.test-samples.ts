import dayjs from 'dayjs/esm';

import { Sexe } from 'app/entities/enumerations/sexe.model';

import { IEmployee, NewEmployee } from './employee.model';

export const sampleWithRequiredData: IEmployee = {
  id: 7813,
  firstName: 'Francine',
  lastName: 'Le roux',
  gender: Sexe['FEMININ'],
  contactNumber: 'Dinar',
};

export const sampleWithPartialData: IEmployee = {
  id: 50464,
  firstName: 'Yves',
  lastName: 'Roger',
  gender: Sexe['MASCULIN'],
  dateOfBirth: dayjs('2023-05-27T04:14'),
  contactNumber: 'zero Liban',
  email: 'Coraline85@hotmail.fr',
  nifCin: 'paradigm',
  baseSalary: 93602,
};

export const sampleWithFullData: IEmployee = {
  id: 93513,
  firstName: 'Oury',
  lastName: 'Gerard',
  gender: Sexe['FEMININ'],
  dateOfBirth: dayjs('2023-05-27T00:59'),
  contactNumber: 'Berkshire b',
  email: 'Avigalle.Gaillard33@hotmail.fr',
  nifCin: 'Loan challenge Iranian',
  address: 'Rubber c',
  baseSalary: 43541,
  commissionRate: 78746,
  salary: 54746,
  hireDate: dayjs('2023-05-27T09:10'),
  terminationDate: dayjs('2023-05-27T09:33'),
};

export const sampleWithNewData: NewEmployee = {
  firstName: 'Astérie',
  lastName: 'Perrin',
  gender: Sexe['MASCULIN'],
  contactNumber: 'Steel',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
