import { IProduct } from 'app/entities/product/product.model';
import { StatusPrice } from 'app/entities/enumerations/status-price.model';

export interface IProductPrice {
  id: number;
  price?: number | null;
  status?: StatusPrice | null;
  product?: Pick<IProduct, 'id' | 'name'> | null;
}

export type NewProductPrice = Omit<IProductPrice, 'id'> & { id: null };
