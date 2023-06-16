import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPrix, NewPrix } from '../prix.model';

export type PartialUpdatePrix = Partial<IPrix> & Pick<IPrix, 'id'>;

type RestOf<T extends IPrix | NewPrix> = Omit<T, 'date'> & {
  date?: string | null;
};

export type RestPrix = RestOf<IPrix>;

export type NewRestPrix = RestOf<NewPrix>;

export type PartialUpdateRestPrix = RestOf<PartialUpdatePrix>;

export type EntityResponseType = HttpResponse<IPrix>;
export type EntityArrayResponseType = HttpResponse<IPrix[]>;

@Injectable({ providedIn: 'root' })
export class PrixService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/prixes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(prix: NewPrix): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prix);
    return this.http.post<RestPrix>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(prix: IPrix): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prix);
    return this.http
      .put<RestPrix>(`${this.resourceUrl}/${this.getPrixIdentifier(prix)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(prix: PartialUpdatePrix): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prix);
    return this.http
      .patch<RestPrix>(`${this.resourceUrl}/${this.getPrixIdentifier(prix)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPrix>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPrix[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPrixIdentifier(prix: Pick<IPrix, 'id'>): number {
    return prix.id;
  }

  comparePrix(o1: Pick<IPrix, 'id'> | null, o2: Pick<IPrix, 'id'> | null): boolean {
    return o1 && o2 ? this.getPrixIdentifier(o1) === this.getPrixIdentifier(o2) : o1 === o2;
  }

  addPrixToCollectionIfMissing<Type extends Pick<IPrix, 'id'>>(
    prixCollection: Type[],
    ...prixesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const prixes: Type[] = prixesToCheck.filter(isPresent);
    if (prixes.length > 0) {
      const prixCollectionIdentifiers = prixCollection.map(prixItem => this.getPrixIdentifier(prixItem)!);
      const prixesToAdd = prixes.filter(prixItem => {
        const prixIdentifier = this.getPrixIdentifier(prixItem);
        if (prixCollectionIdentifiers.includes(prixIdentifier)) {
          return false;
        }
        prixCollectionIdentifiers.push(prixIdentifier);
        return true;
      });
      return [...prixesToAdd, ...prixCollection];
    }
    return prixCollection;
  }

  protected convertDateFromClient<T extends IPrix | NewPrix | PartialUpdatePrix>(prix: T): RestOf<T> {
    return {
      ...prix,
      date: prix.date?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restPrix: RestPrix): IPrix {
    return {
      ...restPrix,
      date: restPrix.date ? dayjs(restPrix.date) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPrix>): HttpResponse<IPrix> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPrix[]>): HttpResponse<IPrix[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
