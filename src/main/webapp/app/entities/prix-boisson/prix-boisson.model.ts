import { IBoisson } from 'app/entities/boisson/boisson.model';

export interface IPrixBoisson {
  id: number;
  prix?: number | null;
  boisson?: Pick<IBoisson, 'id' | 'name'> | null;
}

export type NewPrixBoisson = Omit<IPrixBoisson, 'id'> & { id: null };
