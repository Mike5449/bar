import { IDepot } from "../depot/depot.model";

export interface IClient {
  id: number;
  name?: string | null;
  depot?:IDepot[] | null;
  createdBy?: string | null;
}

export type NewClient = Omit<IClient, 'id'> & { id: null };
