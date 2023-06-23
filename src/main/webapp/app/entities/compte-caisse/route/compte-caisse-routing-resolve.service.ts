import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICompteCaisse } from '../compte-caisse.model';
import { CompteCaisseService } from '../service/compte-caisse.service';

@Injectable({ providedIn: 'root' })
export class CompteCaisseRoutingResolveService implements Resolve<ICompteCaisse | null> {
  constructor(protected service: CompteCaisseService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICompteCaisse | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((compteCaisse: HttpResponse<ICompteCaisse>) => {
          if (compteCaisse.body) {
            return of(compteCaisse.body);
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
