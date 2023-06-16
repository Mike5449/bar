import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPrixBoisson, NewPrixBoisson } from '../prix-boisson.model';

export type PartialUpdatePrixBoisson = Partial<IPrixBoisson> & Pick<IPrixBoisson, 'id'>;

export type EntityResponseType = HttpResponse<IPrixBoisson>;
export type EntityArrayResponseType = HttpResponse<IPrixBoisson[]>;

@Injectable({ providedIn: 'root' })
export class PrixBoissonService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/prix-boissons');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(prixBoisson: NewPrixBoisson): Observable<EntityResponseType> {
    return this.http.post<IPrixBoisson>(this.resourceUrl, prixBoisson, { observe: 'response' });
  }

  update(prixBoisson: IPrixBoisson): Observable<EntityResponseType> {
    return this.http.put<IPrixBoisson>(`${this.resourceUrl}/${this.getPrixBoissonIdentifier(prixBoisson)}`, prixBoisson, {
      observe: 'response',
    });
  }

  partialUpdate(prixBoisson: PartialUpdatePrixBoisson): Observable<EntityResponseType> {
    return this.http.patch<IPrixBoisson>(`${this.resourceUrl}/${this.getPrixBoissonIdentifier(prixBoisson)}`, prixBoisson, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPrixBoisson>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPrixBoisson[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPrixBoissonIdentifier(prixBoisson: Pick<IPrixBoisson, 'id'>): number {
    return prixBoisson.id;
  }

  comparePrixBoisson(o1: Pick<IPrixBoisson, 'id'> | null, o2: Pick<IPrixBoisson, 'id'> | null): boolean {
    return o1 && o2 ? this.getPrixBoissonIdentifier(o1) === this.getPrixBoissonIdentifier(o2) : o1 === o2;
  }

  addPrixBoissonToCollectionIfMissing<Type extends Pick<IPrixBoisson, 'id'>>(
    prixBoissonCollection: Type[],
    ...prixBoissonsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const prixBoissons: Type[] = prixBoissonsToCheck.filter(isPresent);
    if (prixBoissons.length > 0) {
      const prixBoissonCollectionIdentifiers = prixBoissonCollection.map(
        prixBoissonItem => this.getPrixBoissonIdentifier(prixBoissonItem)!
      );
      const prixBoissonsToAdd = prixBoissons.filter(prixBoissonItem => {
        const prixBoissonIdentifier = this.getPrixBoissonIdentifier(prixBoissonItem);
        if (prixBoissonCollectionIdentifiers.includes(prixBoissonIdentifier)) {
          return false;
        }
        prixBoissonCollectionIdentifiers.push(prixBoissonIdentifier);
        return true;
      });
      return [...prixBoissonsToAdd, ...prixBoissonCollection];
    }
    return prixBoissonCollection;
  }
}
