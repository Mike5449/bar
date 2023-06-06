import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ShiftFormService, ShiftFormGroup } from './shift-form.service';
import { IShift } from '../shift.model';
import { ShiftService } from '../service/shift.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { RequestStatus } from 'app/entities/enumerations/request-status.model';

@Component({
  selector: 'jhi-shift-update',
  templateUrl: './shift-update.component.html',
})
export class ShiftUpdateComponent implements OnInit {
  isSaving = false;
  shift: IShift | null = null;
  requestStatusValues = Object.keys(RequestStatus);

  employeesSharedCollection: IEmployee[] = [];

  editForm: ShiftFormGroup = this.shiftFormService.createShiftFormGroup();

  constructor(
    protected shiftService: ShiftService,
    protected shiftFormService: ShiftFormService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shift }) => {
      this.shift = shift;
      if (shift) {
        this.updateForm(shift);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const shift = this.shiftFormService.getShift(this.editForm);
    if (shift.id !== null) {
      this.subscribeToSaveResponse(this.shiftService.update(shift));
    } else {
      this.subscribeToSaveResponse(this.shiftService.create(shift));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IShift>>): void {
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

  protected updateForm(shift: IShift): void {
    this.shift = shift;
    this.shiftFormService.resetForm(this.editForm, shift);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      shift.employee
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) => this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.shift?.employee))
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
