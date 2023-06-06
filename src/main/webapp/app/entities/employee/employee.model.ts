import dayjs from 'dayjs/esm';
import { IJob } from 'app/entities/job/job.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';

export interface IEmployee {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  gender?: Sexe | null;
  dateOfBirth?: dayjs.Dayjs | null;
  contactNumber?: string | null;
  email?: string | null;
  nifCin?: string | null;
  address?: string | null;
  baseSalary?: number | null;
  commissionRate?: number | null;
  salary?: number | null;
  hireDate?: dayjs.Dayjs | null;
  terminationDate?: dayjs.Dayjs | null;
  job?: Pick<IJob, 'id' | 'jobName'> | null;
}

export type NewEmployee = Omit<IEmployee, 'id'> & { id: null };
