import dayjs from 'dayjs/esm';
import { IBoisson } from 'app/entities/boisson/boisson.model';

export interface IPrix {
  id: number;
  prix?: number | null;
  date?: dayjs.Dayjs | null;
  boisson?: Pick<IBoisson, 'id' | 'nom'> | null;
}

export type NewPrix = Omit<IPrix, 'id'> & { id: null };
