import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProductPrice } from '../product-price.model';
import { ProductPriceService } from '../service/product-price.service';

@Injectable({ providedIn: 'root' })
export class ProductPriceRoutingResolveService implements Resolve<IProductPrice | null> {
  constructor(protected service: ProductPriceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProductPrice | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((productPrice: HttpResponse<IProductPrice>) => {
          if (productPrice.body) {
            return of(productPrice.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
