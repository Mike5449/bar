import { IEmployee } from 'app/entities/employee/employee.model';

export interface IDepot {
  id: number;
  amount?: number | null;
  description?: string | null;
  employee?: Pick<IEmployee, 'id' | 'firstName'> | null;
}

export type NewDepot = Omit<IDepot, 'id'> & { id: null };
