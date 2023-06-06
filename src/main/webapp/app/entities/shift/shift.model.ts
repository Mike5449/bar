import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';
import { RequestStatus } from 'app/entities/enumerations/request-status.model';

export interface IShift {
  id: number;
  shiftDate?: dayjs.Dayjs | null;
  shiftType?: RequestStatus | null;
  employee?: Pick<IEmployee, 'id' | 'firstName'> | null;
}

export type NewShift = Omit<IShift, 'id'> & { id: null };
