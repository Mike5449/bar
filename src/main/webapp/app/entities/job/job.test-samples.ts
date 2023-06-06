import { IJob, NewJob } from './job.model';

export const sampleWithRequiredData: IJob = {
  id: 43395,
  jobName: "d'Alésia Rhône-Alpes",
};

export const sampleWithPartialData: IJob = {
  id: 18405,
  jobName: 'Movies',
};

export const sampleWithFullData: IJob = {
  id: 26191,
  jobName: 'Pizza Languedoc-Roussillon streamline',
};

export const sampleWithNewData: NewJob = {
  jobName: 'withdrawal',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
