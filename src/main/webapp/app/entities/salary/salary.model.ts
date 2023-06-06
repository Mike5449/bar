import { IEmployee } from 'app/entities/employee/employee.model';

export interface ISalary {
  id: number;
  month?: number | null;
  year?: number | null;
  amount?: number | null;
  employee?: Pick<IEmployee, 'id' | 'firstName'> | null;
}

export type NewSalary = Omit<ISalary, 'id'> & { id: null };
