import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LeaveRequestFormService } from './leave-request-form.service';
import { LeaveRequestService } from '../service/leave-request.service';
import { ILeaveRequest } from '../leave-request.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { LeaveRequestUpdateComponent } from './leave-request-update.component';

describe('LeaveRequest Management Update Component', () => {
  let comp: LeaveRequestUpdateComponent;
  let fixture: ComponentFixture<LeaveRequestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let leaveRequestFormService: LeaveRequestFormService;
  let leaveRequestService: LeaveRequestService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LeaveRequestUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(LeaveRequestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LeaveRequestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    leaveRequestFormService = TestBed.inject(LeaveRequestFormService);
    leaveRequestService = TestBed.inject(LeaveRequestService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const leaveRequest: ILeaveRequest = { id: 456 };
      const employee: IEmployee = { id: 98126 };
      leaveRequest.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 99495 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ leaveRequest });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const leaveRequest: ILeaveRequest = { id: 456 };
      const employee: IEmployee = { id: 78939 };
      leaveRequest.employee = employee;

      activatedRoute.data = of({ leaveRequest });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.leaveRequest).toEqual(leaveRequest);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILeaveRequest>>();
      const leaveRequest = { id: 123 };
      jest.spyOn(leaveRequestFormService, 'getLeaveRequest').mockReturnValue(leaveRequest);
      jest.spyOn(leaveRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaveRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leaveRequest }));
      saveSubject.complete();

      // THEN
      expect(leaveRequestFormService.getLeaveRequest).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(leaveRequestService.update).toHaveBeenCalledWith(expect.objectContaining(leaveRequest));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILeaveRequest>>();
      const leaveRequest = { id: 123 };
      jest.spyOn(leaveRequestFormService, 'getLeaveRequest').mockReturnValue({ id: null });
      jest.spyOn(leaveRequestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaveRequest: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leaveRequest }));
      saveSubject.complete();

      // THEN
      expect(leaveRequestFormService.getLeaveRequest).toHaveBeenCalled();
      expect(leaveRequestService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILeaveRequest>>();
      const leaveRequest = { id: 123 };
      jest.spyOn(leaveRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaveRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(leaveRequestService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEmployee', () => {
      it('Should forward to employeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
