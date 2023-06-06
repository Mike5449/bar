export interface IPoste {
  id: number;
  posteName?: string | null;
}

export type NewPoste = Omit<IPoste, 'id'> & { id: null };
