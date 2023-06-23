import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompteCaisse, NewCompteCaisse } from '../compte-caisse.model';

export type PartialUpdateCompteCaisse = Partial<ICompteCaisse> & Pick<ICompteCaisse, 'id'>;

export type EntityResponseType = HttpResponse<ICompteCaisse>;
export type EntityArrayResponseType = HttpResponse<ICompteCaisse[]>;

@Injectable({ providedIn: 'root' })
export class CompteCaisseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/compte-caisses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(compteCaisse: NewCompteCaisse): Observable<EntityResponseType> {
    return this.http.post<ICompteCaisse>(this.resourceUrl, compteCaisse, { observe: 'response' });
  }

  update(compteCaisse: ICompteCaisse): Observable<EntityResponseType> {
    return this.http.put<ICompteCaisse>(`${this.resourceUrl}/${this.getCompteCaisseIdentifier(compteCaisse)}`, compteCaisse, {
      observe: 'response',
    });
  }

  updateInjection(compteCaisse?: ICompteCaisse , injection?:number): Observable<EntityResponseType> {
    return this.http.put<ICompteCaisse>(`${this.resourceUrl}/injection/${compteCaisse?.id}/${injection}`, compteCaisse, {
      observe: 'response',
    });
  }

  updatePret(compteCaisse?: ICompteCaisse , pret?:number): Observable<EntityResponseType> {
    return this.http.put<ICompteCaisse>(`${this.resourceUrl}/pret/${compteCaisse?.id}/${pret}`, compteCaisse, {
      observe: 'response',
    });
  }

  setControl(compteCaisse?: ICompteCaisse , versement?:number): Observable<EntityResponseType> {
    return this.http.put<ICompteCaisse>(`${this.resourceUrl}/control/${compteCaisse?.id}/${versement}`, compteCaisse, {
      observe: 'response',
    });
  }


  partialUpdate(compteCaisse: PartialUpdateCompteCaisse): Observable<EntityResponseType> {
    return this.http.patch<ICompteCaisse>(`${this.resourceUrl}/${this.getCompteCaisseIdentifier(compteCaisse)}`, compteCaisse, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICompteCaisse>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICompteCaisse[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  getByEmployeAndActiveCaise(): Observable<EntityArrayResponseType> {
    
    return this.http.get<ICompteCaisse[]>(`${this.resourceUrl}/active`, {observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCompteCaisseIdentifier(compteCaisse: Pick<ICompteCaisse, 'id'>): number {
    return compteCaisse.id;
  }

  compareCompteCaisse(o1: Pick<ICompteCaisse, 'id'> | null, o2: Pick<ICompteCaisse, 'id'> | null): boolean {
    return o1 && o2 ? this.getCompteCaisseIdentifier(o1) === this.getCompteCaisseIdentifier(o2) : o1 === o2;
  }

  addCompteCaisseToCollectionIfMissing<Type extends Pick<ICompteCaisse, 'id'>>(
    compteCaisseCollection: Type[],
    ...compteCaissesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const compteCaisses: Type[] = compteCaissesToCheck.filter(isPresent);
    if (compteCaisses.length > 0) {
      const compteCaisseCollectionIdentifiers = compteCaisseCollection.map(
        compteCaisseItem => this.getCompteCaisseIdentifier(compteCaisseItem)!
      );
      const compteCaissesToAdd = compteCaisses.filter(compteCaisseItem => {
        const compteCaisseIdentifier = this.getCompteCaisseIdentifier(compteCaisseItem);
        if (compteCaisseCollectionIdentifiers.includes(compteCaisseIdentifier)) {
          return false;
        }
        compteCaisseCollectionIdentifiers.push(compteCaisseIdentifier);
        return true;
      });
      return [...compteCaissesToAdd, ...compteCaisseCollection];
    }
    return compteCaisseCollection;
  }
}
