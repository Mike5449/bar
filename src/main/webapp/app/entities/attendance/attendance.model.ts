import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface IAttendance {
  id: number;
  date?: dayjs.Dayjs | null;
  isPresent?: boolean | null;
  employee?: Pick<IEmployee, 'id' | 'firstName'> | null;
}

export type NewAttendance = Omit<IAttendance, 'id'> & { id: null };
