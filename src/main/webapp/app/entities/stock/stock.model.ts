import { IProduct } from 'app/entities/product/product.model';

export interface IStock {
  id: number;
  quantity?: number | null;
  buyPrice?: number | null;
  quantityMinimal?: number | null;
  product?: Pick<IProduct, 'id' | 'name' | 'price' | 'section' | 'type' | 'image'  | 'imageContentType'> | null;
}

export type NewStock = Omit<IStock, 'id'> & { id: null };
