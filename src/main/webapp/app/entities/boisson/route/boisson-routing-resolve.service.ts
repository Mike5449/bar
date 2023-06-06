import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBoisson } from '../boisson.model';
import { BoissonService } from '../service/boisson.service';

@Injectable({ providedIn: 'root' })
export class BoissonRoutingResolveService implements Resolve<IBoisson | null> {
  constructor(protected service: BoissonService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBoisson | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((boisson: HttpResponse<IBoisson>) => {
          if (boisson.body) {
            return of(boisson.body);
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
