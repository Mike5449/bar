import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISalary } from '../salary.model';
import { SalaryService } from '../service/salary.service';

@Injectable({ providedIn: 'root' })
export class SalaryRoutingResolveService implements Resolve<ISalary | null> {
  constructor(protected service: SalaryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISalary | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((salary: HttpResponse<ISalary>) => {
          if (salary.body) {
            return of(salary.body);
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
