import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPoste } from '../poste.model';
import { PosteService } from '../service/poste.service';

@Injectable({ providedIn: 'root' })
export class PosteRoutingResolveService implements Resolve<IPoste | null> {
  constructor(protected service: PosteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPoste | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((poste: HttpResponse<IPoste>) => {
          if (poste.body) {
            return of(poste.body);
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
