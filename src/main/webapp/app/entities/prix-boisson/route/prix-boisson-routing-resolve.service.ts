import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrixBoisson } from '../prix-boisson.model';
import { PrixBoissonService } from '../service/prix-boisson.service';

@Injectable({ providedIn: 'root' })
export class PrixBoissonRoutingResolveService implements Resolve<IPrixBoisson | null> {
  constructor(protected service: PrixBoissonService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrixBoisson | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((prixBoisson: HttpResponse<IPrixBoisson>) => {
          if (prixBoisson.body) {
            return of(prixBoisson.body);
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
