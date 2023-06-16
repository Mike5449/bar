import { Categorie } from 'app/entities/enumerations/categorie.model';
import { Section } from 'app/entities/enumerations/section.model';

export interface IProduct {
  id: number;
  name?: string | null;
  price?:number | null;
  image?: string | null;
  imageContentType?: string | null;
  type?: Categorie | null;
  section?: Section | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };
