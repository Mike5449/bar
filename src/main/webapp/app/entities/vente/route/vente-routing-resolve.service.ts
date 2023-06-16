import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVente } from '../vente.model';
import { VenteService } from '../service/vente.service';

@Injectable({ providedIn: 'root' })
export class VenteRoutingResolveService implements Resolve<IVente | null> {
  constructor(protected service: VenteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVente | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vente: HttpResponse<IVente>) => {
          if (vente.body) {
            return of(vente.body);
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
