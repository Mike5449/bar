import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SalaryFormService } from './salary-form.service';
import { SalaryService } from '../service/salary.service';
import { ISalary } from '../salary.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { SalaryUpdateComponent } from './salary-update.component';

describe('Salary Management Update Component', () => {
  let comp: SalaryUpdateComponent;
  let fixture: ComponentFixture<SalaryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let salaryFormService: SalaryFormService;
  let salaryService: SalaryService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SalaryUpdateComponent],
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
      .overrideTemplate(SalaryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SalaryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    salaryFormService = TestBed.inject(SalaryFormService);
    salaryService = TestBed.inject(SalaryService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const salary: ISalary = { id: 456 };
      const employee: IEmployee = { id: 2658 };
      salary.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 3269 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ salary });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const salary: ISalary = { id: 456 };
      const employee: IEmployee = { id: 67725 };
      salary.employee = employee;

      activatedRoute.data = of({ salary });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.salary).toEqual(salary);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISalary>>();
      const salary = { id: 123 };
      jest.spyOn(salaryFormService, 'getSalary').mockReturnValue(salary);
      jest.spyOn(salaryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: salary }));
      saveSubject.complete();

      // THEN
      expect(salaryFormService.getSalary).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(salaryService.update).toHaveBeenCalledWith(expect.objectContaining(salary));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISalary>>();
      const salary = { id: 123 };
      jest.spyOn(salaryFormService, 'getSalary').mockReturnValue({ id: null });
      jest.spyOn(salaryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salary: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: salary }));
      saveSubject.complete();

      // THEN
      expect(salaryFormService.getSalary).toHaveBeenCalled();
      expect(salaryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISalary>>();
      const salary = { id: 123 };
      jest.spyOn(salaryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(salaryService.update).toHaveBeenCalled();
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
