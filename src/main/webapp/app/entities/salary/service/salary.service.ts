import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISalary, NewSalary } from '../salary.model';

export type PartialUpdateSalary = Partial<ISalary> & Pick<ISalary, 'id'>;

export type EntityResponseType = HttpResponse<ISalary>;
export type EntityArrayResponseType = HttpResponse<ISalary[]>;

@Injectable({ providedIn: 'root' })
export class SalaryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/salaries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(salary: NewSalary): Observable<EntityResponseType> {
    return this.http.post<ISalary>(this.resourceUrl, salary, { observe: 'response' });
  }

  update(salary: ISalary): Observable<EntityResponseType> {
    return this.http.put<ISalary>(`${this.resourceUrl}/${this.getSalaryIdentifier(salary)}`, salary, { observe: 'response' });
  }

  partialUpdate(salary: PartialUpdateSalary): Observable<EntityResponseType> {
    return this.http.patch<ISalary>(`${this.resourceUrl}/${this.getSalaryIdentifier(salary)}`, salary, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISalary>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISalary[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSalaryIdentifier(salary: Pick<ISalary, 'id'>): number {
    return salary.id;
  }

  compareSalary(o1: Pick<ISalary, 'id'> | null, o2: Pick<ISalary, 'id'> | null): boolean {
    return o1 && o2 ? this.getSalaryIdentifier(o1) === this.getSalaryIdentifier(o2) : o1 === o2;
  }

  addSalaryToCollectionIfMissing<Type extends Pick<ISalary, 'id'>>(
    salaryCollection: Type[],
    ...salariesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const salaries: Type[] = salariesToCheck.filter(isPresent);
    if (salaries.length > 0) {
      const salaryCollectionIdentifiers = salaryCollection.map(salaryItem => this.getSalaryIdentifier(salaryItem)!);
      const salariesToAdd = salaries.filter(salaryItem => {
        const salaryIdentifier = this.getSalaryIdentifier(salaryItem);
        if (salaryCollectionIdentifiers.includes(salaryIdentifier)) {
          return false;
        }
        salaryCollectionIdentifiers.push(salaryIdentifier);
        return true;
      });
      return [...salariesToAdd, ...salaryCollection];
    }
    return salaryCollection;
  }
}
