import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';
import { RequestStatus } from 'app/entities/enumerations/request-status.model';

export interface ILeaveRequest {
  id: number;
  startDate?: dayjs.Dayjs | null;
  enDate?: dayjs.Dayjs | null;
  requestStatus?: RequestStatus | null;
  employee?: Pick<IEmployee, 'id' | 'firstName'> | null;
}

export type NewLeaveRequest = Omit<ILeaveRequest, 'id'> & { id: null };
