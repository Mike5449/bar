import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILeaveRequest } from '../leave-request.model';

@Component({
  selector: 'jhi-leave-request-detail',
  templateUrl: './leave-request-detail.component.html',
})
export class LeaveRequestDetailComponent implements OnInit {
  leaveRequest: ILeaveRequest | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaveRequest }) => {
      this.leaveRequest = leaveRequest;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
