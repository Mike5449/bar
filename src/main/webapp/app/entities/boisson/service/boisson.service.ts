import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBoisson, NewBoisson } from '../boisson.model';

export type PartialUpdateBoisson = Partial<IBoisson> & Pick<IBoisson, 'id'>;

export type EntityResponseType = HttpResponse<IBoisson>;
export type EntityArrayResponseType = HttpResponse<IBoisson[]>;

@Injectable({ providedIn: 'root' })
export class BoissonService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/boissons');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(boisson: NewBoisson): Observable<EntityResponseType> {
    return this.http.post<IBoisson>(this.resourceUrl, boisson, { observe: 'response' });
  }

  update(boisson: IBoisson): Observable<EntityResponseType> {
    return this.http.put<IBoisson>(`${this.resourceUrl}/${this.getBoissonIdentifier(boisson)}`, boisson, { observe: 'response' });
  }

  partialUpdate(boisson: PartialUpdateBoisson): Observable<EntityResponseType> {
    return this.http.patch<IBoisson>(`${this.resourceUrl}/${this.getBoissonIdentifier(boisson)}`, boisson, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBoisson>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBoisson[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBoissonIdentifier(boisson: Pick<IBoisson, 'id'>): number {
    return boisson.id;
  }

  compareBoisson(o1: Pick<IBoisson, 'id'> | null, o2: Pick<IBoisson, 'id'> | null): boolean {
    return o1 && o2 ? this.getBoissonIdentifier(o1) === this.getBoissonIdentifier(o2) : o1 === o2;
  }

  addBoissonToCollectionIfMissing<Type extends Pick<IBoisson, 'id'>>(
    boissonCollection: Type[],
    ...boissonsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const boissons: Type[] = boissonsToCheck.filter(isPresent);
    if (boissons.length > 0) {
      const boissonCollectionIdentifiers = boissonCollection.map(boissonItem => this.getBoissonIdentifier(boissonItem)!);
      const boissonsToAdd = boissons.filter(boissonItem => {
        const boissonIdentifier = this.getBoissonIdentifier(boissonItem);
        if (boissonCollectionIdentifiers.includes(boissonIdentifier)) {
          return false;
        }
        boissonCollectionIdentifiers.push(boissonIdentifier);
        return true;
      });
      return [...boissonsToAdd, ...boissonCollection];
    }
    return boissonCollection;
  }
}
