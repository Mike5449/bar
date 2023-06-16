import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVente, NewVente } from '../vente.model';

export type PartialUpdateVente = Partial<IVente> & Pick<IVente, 'id'>;

export type EntityResponseType = HttpResponse<IVente>;
export type EntityArrayResponseType = HttpResponse<IVente[]>;

@Injectable({ providedIn: 'root' })
export class VenteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ventes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vente: NewVente): Observable<EntityResponseType> {
    return this.http.post<IVente>(this.resourceUrl, vente, { observe: 'response' });
  }

  update(vente: IVente): Observable<EntityResponseType> {
    return this.http.put<IVente>(`${this.resourceUrl}/${this.getVenteIdentifier(vente)}`, vente, { observe: 'response' });
  }

  partialUpdate(vente: PartialUpdateVente): Observable<EntityResponseType> {
    return this.http.patch<IVente>(`${this.resourceUrl}/${this.getVenteIdentifier(vente)}`, vente, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVente>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVente[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVenteIdentifier(vente: Pick<IVente, 'id'>): number {
    return vente.id;
  }

  compareVente(o1: Pick<IVente, 'id'> | null, o2: Pick<IVente, 'id'> | null): boolean {
    return o1 && o2 ? this.getVenteIdentifier(o1) === this.getVenteIdentifier(o2) : o1 === o2;
  }

  addVenteToCollectionIfMissing<Type extends Pick<IVente, 'id'>>(
    venteCollection: Type[],
    ...ventesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ventes: Type[] = ventesToCheck.filter(isPresent);
    if (ventes.length > 0) {
      const venteCollectionIdentifiers = venteCollection.map(venteItem => this.getVenteIdentifier(venteItem)!);
      const ventesToAdd = ventes.filter(venteItem => {
        const venteIdentifier = this.getVenteIdentifier(venteItem);
        if (venteCollectionIdentifiers.includes(venteIdentifier)) {
          return false;
        }
        venteCollectionIdentifiers.push(venteIdentifier);
        return true;
      });
      return [...ventesToAdd, ...venteCollection];
    }
    return venteCollection;
  }
}
