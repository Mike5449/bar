import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrix } from '../prix.model';
import { PrixService } from '../service/prix.service';

@Injectable({ providedIn: 'root' })
export class PrixRoutingResolveService implements Resolve<IPrix | null> {
  constructor(protected service: PrixService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrix | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((prix: HttpResponse<IPrix>) => {
          if (prix.body) {
            return of(prix.body);
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
