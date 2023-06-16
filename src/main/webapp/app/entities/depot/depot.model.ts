import { IEmployee } from 'app/entities/employee/employee.model';
import { IClient } from 'app/entities/client/client.model';

export interface IDepot {
  id: number;
  amount?: number | null;
  description?: string | null;
  employee?: Pick<IEmployee, 'id' | 'firstName'> | null;
  client?: Pick<IClient, 'id' | 'name'> | null;
}

export type NewDepot = Omit<IDepot, 'id'> & { id: null };
