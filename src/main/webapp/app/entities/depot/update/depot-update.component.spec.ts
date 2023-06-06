import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DepotFormService } from './depot-form.service';
import { DepotService } from '../service/depot.service';
import { IDepot } from '../depot.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { DepotUpdateComponent } from './depot-update.component';

describe('Depot Management Update Component', () => {
  let comp: DepotUpdateComponent;
  let fixture: ComponentFixture<DepotUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let depotFormService: DepotFormService;
  let depotService: DepotService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DepotUpdateComponent],
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
      .overrideTemplate(DepotUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DepotUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    depotFormService = TestBed.inject(DepotFormService);
    depotService = TestBed.inject(DepotService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const depot: IDepot = { id: 456 };
      const employee: IEmployee = { id: 78918 };
      depot.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 31644 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ depot });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const depot: IDepot = { id: 456 };
      const employee: IEmployee = { id: 51019 };
      depot.employee = employee;

      activatedRoute.data = of({ depot });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.depot).toEqual(depot);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepot>>();
      const depot = { id: 123 };
      jest.spyOn(depotFormService, 'getDepot').mockReturnValue(depot);
      jest.spyOn(depotService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ depot });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: depot }));
      saveSubject.complete();

      // THEN
      expect(depotFormService.getDepot).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(depotService.update).toHaveBeenCalledWith(expect.objectContaining(depot));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepot>>();
      const depot = { id: 123 };
      jest.spyOn(depotFormService, 'getDepot').mockReturnValue({ id: null });
      jest.spyOn(depotService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ depot: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: depot }));
      saveSubject.complete();

      // THEN
      expect(depotFormService.getDepot).toHaveBeenCalled();
      expect(depotService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepot>>();
      const depot = { id: 123 };
      jest.spyOn(depotService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ depot });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(depotService.update).toHaveBeenCalled();
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
