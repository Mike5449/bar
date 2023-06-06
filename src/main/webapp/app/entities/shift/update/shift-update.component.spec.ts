import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ShiftFormService } from './shift-form.service';
import { ShiftService } from '../service/shift.service';
import { IShift } from '../shift.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { ShiftUpdateComponent } from './shift-update.component';

describe('Shift Management Update Component', () => {
  let comp: ShiftUpdateComponent;
  let fixture: ComponentFixture<ShiftUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let shiftFormService: ShiftFormService;
  let shiftService: ShiftService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ShiftUpdateComponent],
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
      .overrideTemplate(ShiftUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ShiftUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    shiftFormService = TestBed.inject(ShiftFormService);
    shiftService = TestBed.inject(ShiftService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const shift: IShift = { id: 456 };
      const employee: IEmployee = { id: 2452 };
      shift.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 72509 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ shift });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const shift: IShift = { id: 456 };
      const employee: IEmployee = { id: 65660 };
      shift.employee = employee;

      activatedRoute.data = of({ shift });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.shift).toEqual(shift);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IShift>>();
      const shift = { id: 123 };
      jest.spyOn(shiftFormService, 'getShift').mockReturnValue(shift);
      jest.spyOn(shiftService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shift });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: shift }));
      saveSubject.complete();

      // THEN
      expect(shiftFormService.getShift).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(shiftService.update).toHaveBeenCalledWith(expect.objectContaining(shift));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IShift>>();
      const shift = { id: 123 };
      jest.spyOn(shiftFormService, 'getShift').mockReturnValue({ id: null });
      jest.spyOn(shiftService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shift: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: shift }));
      saveSubject.complete();

      // THEN
      expect(shiftFormService.getShift).toHaveBeenCalled();
      expect(shiftService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IShift>>();
      const shift = { id: 123 };
      jest.spyOn(shiftService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shift });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(shiftService.update).toHaveBeenCalled();
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
