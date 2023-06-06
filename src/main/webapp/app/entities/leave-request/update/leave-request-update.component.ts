import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { LeaveRequestFormService, LeaveRequestFormGroup } from './leave-request-form.service';
import { ILeaveRequest } from '../leave-request.model';
import { LeaveRequestService } from '../service/leave-request.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { RequestStatus } from 'app/entities/enumerations/request-status.model';

@Component({
  selector: 'jhi-leave-request-update',
  templateUrl: './leave-request-update.component.html',
})
export class LeaveRequestUpdateComponent implements OnInit {
  isSaving = false;
  leaveRequest: ILeaveRequest | null = null;
  requestStatusValues = Object.keys(RequestStatus);

  employeesSharedCollection: IEmployee[] = [];

  editForm: LeaveRequestFormGroup = this.leaveRequestFormService.createLeaveRequestFormGroup();

  constructor(
    protected leaveRequestService: LeaveRequestService,
    protected leaveRequestFormService: LeaveRequestFormService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaveRequest }) => {
      this.leaveRequest = leaveRequest;
      if (leaveRequest) {
        this.updateForm(leaveRequest);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const leaveRequest = this.leaveRequestFormService.getLeaveRequest(this.editForm);
    if (leaveRequest.id !== null) {
      this.subscribeToSaveResponse(this.leaveRequestService.update(leaveRequest));
    } else {
      this.subscribeToSaveResponse(this.leaveRequestService.create(leaveRequest));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeaveRequest>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(leaveRequest: ILeaveRequest): void {
    this.leaveRequest = leaveRequest;
    this.leaveRequestFormService.resetForm(this.editForm, leaveRequest);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      leaveRequest.employee
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.leaveRequest?.employee)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
