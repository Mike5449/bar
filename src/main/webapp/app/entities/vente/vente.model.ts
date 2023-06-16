import { IEmployee } from 'app/entities/employee/employee.model';
import { IClient } from 'app/entities/client/client.model';
import { IDepot } from 'app/entities/depot/depot.model';
import { IBoisson } from 'app/entities/boisson/boisson.model';
import { StatusVente } from 'app/entities/enumerations/status-vente.model';

export interface IVente {
  id: number;
  quantite?: number | null;
  status?: StatusVente | null;
  employee?: Pick<IEmployee, 'id' | 'firstName'> | null;
  client?: Pick<IClient, 'id' | 'name'> | null;
  depot?: Pick<IDepot, 'id' | 'montant'> | null;
  boisson?: Pick<IBoisson, 'id' | 'name'> | null;
}

export type NewVente = Omit<IVente, 'id'> & { id: null };
