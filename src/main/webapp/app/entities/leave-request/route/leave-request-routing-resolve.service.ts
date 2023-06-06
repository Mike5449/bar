import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeaveRequest } from '../leave-request.model';
import { LeaveRequestService } from '../service/leave-request.service';

@Injectable({ providedIn: 'root' })
export class LeaveRequestRoutingResolveService implements Resolve<ILeaveRequest | null> {
  constructor(protected service: LeaveRequestService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeaveRequest | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((leaveRequest: HttpResponse<ILeaveRequest>) => {
          if (leaveRequest.body) {
            return of(leaveRequest.body);
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
