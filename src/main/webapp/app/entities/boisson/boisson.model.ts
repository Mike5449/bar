import { Categorie } from 'app/entities/enumerations/categorie.model';

export interface IBoisson {
  id: number;
  name?: string | null;
  image?: string | null;
  type?: Categorie | null;
}

export type NewBoisson = Omit<IBoisson, 'id'> & { id: null };
