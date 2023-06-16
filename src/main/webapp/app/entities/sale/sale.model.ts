import { IEmployee } from 'app/entities/employee/employee.model';
import { IClient } from 'app/entities/client/client.model';
import { IDepot } from 'app/entities/depot/depot.model';
import { IProduct } from 'app/entities/product/product.model';
import { IProductPrice } from 'app/entities/product-price/product-price.model';
import { StatusVente } from 'app/entities/enumerations/status-vente.model';

export interface ISale {
  id: number;
  quantity?: number | null;
  unitPrice?: number | null;
  amountTotal?: number | null;
  status?: StatusVente | null;
  employee?: Pick<IEmployee, 'id' | 'firstName'> | null;
  client?: Pick<IClient, 'id' | 'name' | 'depot'> | null;
  depot?: Pick<IDepot, 'id' | 'amount'> | null;
  product?: Pick<IProduct, 'id' | 'name' | 'price'> | null;
  currentPrice?: Pick<IProductPrice, 'id'> | null;
}

export type NewSale = Omit<ISale, 'id'> & { id: null };
