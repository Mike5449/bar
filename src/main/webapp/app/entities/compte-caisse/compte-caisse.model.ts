import { StatusCaisse } from 'app/entities/enumerations/status-caisse.model';
import { IEmployee } from '../employee/employee.model';

export interface ICompteCaisse {
  id: number;
  injection?: number | null;
  sale?: number | null;
  cancel?: number | null;
  cash?: number | null;
  pret?: number | null;
  balance?: number | null;
  aVerser?: number | null;
  status?: StatusCaisse | null;
  name?: string | null;
  employee?:IEmployee | null;
}

export type NewCompteCaisse = Omit<ICompteCaisse, 'id'> & { id: null };
