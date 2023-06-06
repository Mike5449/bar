export interface IJob {
  id: number;
  jobName?: string | null;
}

export type NewJob = Omit<IJob, 'id'> & { id: null };
