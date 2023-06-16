import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VenteFormService } from './vente-form.service';
import { VenteService } from '../service/vente.service';
import { IVente } from '../vente.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { IDepot } from 'app/entities/depot/depot.model';
import { DepotService } from 'app/entities/depot/service/depot.service';
import { IBoisson } from 'app/entities/boisson/boisson.model';
import { BoissonService } from 'app/entities/boisson/service/boisson.service';

import { VenteUpdateComponent } from './vente-update.component';

describe('Vente Management Update Component', () => {
  let comp: VenteUpdateComponent;
  let fixture: ComponentFixture<VenteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let venteFormService: VenteFormService;
  let venteService: VenteService;
  let employeeService: EmployeeService;
  let clientService: ClientService;
  let depotService: DepotService;
  let boissonService: BoissonService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VenteUpdateComponent],
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
      .overrideTemplate(VenteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VenteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    venteFormService = TestBed.inject(VenteFormService);
    venteService = TestBed.inject(VenteService);
    employeeService = TestBed.inject(EmployeeService);
    clientService = TestBed.inject(ClientService);
    depotService = TestBed.inject(DepotService);
    boissonService = TestBed.inject(BoissonService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const vente: IVente = { id: 456 };
      const employee: IEmployee = { id: 39803 };
      vente.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 82474 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vente });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Client query and add missing value', () => {
      const vente: IVente = { id: 456 };
      const client: IClient = { id: 72665 };
      vente.client = client;

      const clientCollection: IClient[] = [{ id: 26669 }];
      jest.spyOn(clientService, 'query').mockReturnValue(of(new HttpResponse({ body: clientCollection })));
      const additionalClients = [client];
      const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
      jest.spyOn(clientService, 'addClientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vente });
      comp.ngOnInit();

      expect(clientService.query).toHaveBeenCalled();
      expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(
        clientCollection,
        ...additionalClients.map(expect.objectContaining)
      );
      expect(comp.clientsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Depot query and add missing value', () => {
      const vente: IVente = { id: 456 };
      const depot: IDepot = { id: 62712 };
      vente.depot = depot;

      const depotCollection: IDepot[] = [{ id: 32440 }];
      jest.spyOn(depotService, 'query').mockReturnValue(of(new HttpResponse({ body: depotCollection })));
      const additionalDepots = [depot];
      const expectedCollection: IDepot[] = [...additionalDepots, ...depotCollection];
      jest.spyOn(depotService, 'addDepotToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vente });
      comp.ngOnInit();

      expect(depotService.query).toHaveBeenCalled();
      expect(depotService.addDepotToCollectionIfMissing).toHaveBeenCalledWith(
        depotCollection,
        ...additionalDepots.map(expect.objectContaining)
      );
      expect(comp.depotsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Boisson query and add missing value', () => {
      const vente: IVente = { id: 456 };
      const boisson: IBoisson = { id: 76554 };
      vente.boisson = boisson;

      const boissonCollection: IBoisson[] = [{ id: 16300 }];
      jest.spyOn(boissonService, 'query').mockReturnValue(of(new HttpResponse({ body: boissonCollection })));
      const additionalBoissons = [boisson];
      const expectedCollection: IBoisson[] = [...additionalBoissons, ...boissonCollection];
      jest.spyOn(boissonService, 'addBoissonToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vente });
      comp.ngOnInit();

      expect(boissonService.query).toHaveBeenCalled();
      expect(boissonService.addBoissonToCollectionIfMissing).toHaveBeenCalledWith(
        boissonCollection,
        ...additionalBoissons.map(expect.objectContaining)
      );
      expect(comp.boissonsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const vente: IVente = { id: 456 };
      const employee: IEmployee = { id: 4041 };
      vente.employee = employee;
      const client: IClient = { id: 58785 };
      vente.client = client;
      const depot: IDepot = { id: 5627 };
      vente.depot = depot;
      const boisson: IBoisson = { id: 83228 };
      vente.boisson = boisson;

      activatedRoute.data = of({ vente });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.clientsSharedCollection).toContain(client);
      expect(comp.depotsSharedCollection).toContain(depot);
      expect(comp.boissonsSharedCollection).toContain(boisson);
      expect(comp.vente).toEqual(vente);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVente>>();
      const vente = { id: 123 };
      jest.spyOn(venteFormService, 'getVente').mockReturnValue(vente);
      jest.spyOn(venteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vente }));
      saveSubject.complete();

      // THEN
      expect(venteFormService.getVente).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(venteService.update).toHaveBeenCalledWith(expect.objectContaining(vente));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVente>>();
      const vente = { id: 123 };
      jest.spyOn(venteFormService, 'getVente').mockReturnValue({ id: null });
      jest.spyOn(venteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vente: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vente }));
      saveSubject.complete();

      // THEN
      expect(venteFormService.getVente).toHaveBeenCalled();
      expect(venteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVente>>();
      const vente = { id: 123 };
      jest.spyOn(venteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(venteService.update).toHaveBeenCalled();
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

    describe('compareClient', () => {
      it('Should forward to clientService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(clientService, 'compareClient');
        comp.compareClient(entity, entity2);
        expect(clientService.compareClient).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDepot', () => {
      it('Should forward to depotService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(depotService, 'compareDepot');
        comp.compareDepot(entity, entity2);
        expect(depotService.compareDepot).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareBoisson', () => {
      it('Should forward to boissonService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(boissonService, 'compareBoisson');
        comp.compareBoisson(entity, entity2);
        expect(boissonService.compareBoisson).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
