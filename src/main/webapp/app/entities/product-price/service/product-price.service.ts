import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductPrice, NewProductPrice } from '../product-price.model';

export type PartialUpdateProductPrice = Partial<IProductPrice> & Pick<IProductPrice, 'id'>;

export type EntityResponseType = HttpResponse<IProductPrice>;
export type EntityArrayResponseType = HttpResponse<IProductPrice[]>;

@Injectable({ providedIn: 'root' })
export class ProductPriceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/product-prices');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(productPrice: NewProductPrice): Observable<EntityResponseType> {
    return this.http.post<IProductPrice>(this.resourceUrl, productPrice, { observe: 'response' });
  }

  update(productPrice: IProductPrice): Observable<EntityResponseType> {
    return this.http.put<IProductPrice>(`${this.resourceUrl}/${this.getProductPriceIdentifier(productPrice)}`, productPrice, {
      observe: 'response',
    });
  }

  partialUpdate(productPrice: PartialUpdateProductPrice): Observable<EntityResponseType> {
    return this.http.patch<IProductPrice>(`${this.resourceUrl}/${this.getProductPriceIdentifier(productPrice)}`, productPrice, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProductPrice>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProductPrice[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProductPriceIdentifier(productPrice: Pick<IProductPrice, 'id'>): number {
    return productPrice.id;
  }

  compareProductPrice(o1: Pick<IProductPrice, 'id'> | null, o2: Pick<IProductPrice, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductPriceIdentifier(o1) === this.getProductPriceIdentifier(o2) : o1 === o2;
  }

  addProductPriceToCollectionIfMissing<Type extends Pick<IProductPrice, 'id'>>(
    productPriceCollection: Type[],
    ...productPricesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productPrices: Type[] = productPricesToCheck.filter(isPresent);
    if (productPrices.length > 0) {
      const productPriceCollectionIdentifiers = productPriceCollection.map(
        productPriceItem => this.getProductPriceIdentifier(productPriceItem)!
      );
      const productPricesToAdd = productPrices.filter(productPriceItem => {
        const productPriceIdentifier = this.getProductPriceIdentifier(productPriceItem);
        if (productPriceCollectionIdentifiers.includes(productPriceIdentifier)) {
          return false;
        }
        productPriceCollectionIdentifiers.push(productPriceIdentifier);
        return true;
      });
      return [...productPricesToAdd, ...productPriceCollection];
    }
    return productPriceCollection;
  }
}
