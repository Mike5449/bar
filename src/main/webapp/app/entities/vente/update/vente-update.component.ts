import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { VenteFormService, VenteFormGroup } from './vente-form.service';
import { IVente } from '../vente.model';
import { VenteService } from '../service/vente.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { IDepot } from 'app/entities/depot/depot.model';
import { DepotService } from 'app/entities/depot/service/depot.service';
import { IBoisson } from 'app/entities/boisson/boisson.model';
import { BoissonService } from 'app/entities/boisson/service/boisson.service';
import { StatusVente } from 'app/entities/enumerations/status-vente.model';

@Component({
  selector: 'jhi-vente-update',
  templateUrl: './vente-update.component.html',
})
export class VenteUpdateComponent implements OnInit {
  isSaving = false;
  vente: IVente | null = null;
  statusVenteValues = Object.keys(StatusVente);

  employeesSharedCollection: IEmployee[] = [];
  clientsSharedCollection: IClient[] = [];
  depotsSharedCollection: IDepot[] = [];
  boissonsSharedCollection: IBoisson[] = [];

  editForm: VenteFormGroup = this.venteFormService.createVenteFormGroup();

  constructor(
    protected venteService: VenteService,
    protected venteFormService: VenteFormService,
    protected employeeService: EmployeeService,
    protected clientService: ClientService,
    protected depotService: DepotService,
    protected boissonService: BoissonService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareClient = (o1: IClient | null, o2: IClient | null): boolean => this.clientService.compareClient(o1, o2);

  compareDepot = (o1: IDepot | null, o2: IDepot | null): boolean => this.depotService.compareDepot(o1, o2);

  compareBoisson = (o1: IBoisson | null, o2: IBoisson | null): boolean => this.boissonService.compareBoisson(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vente }) => {
      this.vente = vente;
      if (vente) {
        this.updateForm(vente);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vente = this.venteFormService.getVente(this.editForm);
    if (vente.id !== null) {
      this.subscribeToSaveResponse(this.venteService.update(vente));
    } else {
      this.subscribeToSaveResponse(this.venteService.create(vente));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVente>>): void {
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

  protected updateForm(vente: IVente): void {
    this.vente = vente;
    this.venteFormService.resetForm(this.editForm, vente);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      vente.employee
    );
    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing<IClient>(this.clientsSharedCollection, vente.client);
    this.depotsSharedCollection = this.depotService.addDepotToCollectionIfMissing<IDepot>(this.depotsSharedCollection, vente.depot);
    this.boissonsSharedCollection = this.boissonService.addBoissonToCollectionIfMissing<IBoisson>(
      this.boissonsSharedCollection,
      vente.boisson
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) => this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.vente?.employee))
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing<IClient>(clients, this.vente?.client)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));

    this.depotService
      .query()
      .pipe(map((res: HttpResponse<IDepot[]>) => res.body ?? []))
      .pipe(map((depots: IDepot[]) => this.depotService.addDepotToCollectionIfMissing<IDepot>(depots, this.vente?.depot)))
      .subscribe((depots: IDepot[]) => (this.depotsSharedCollection = depots));

    this.boissonService
      .query()
      .pipe(map((res: HttpResponse<IBoisson[]>) => res.body ?? []))
      .pipe(map((boissons: IBoisson[]) => this.boissonService.addBoissonToCollectionIfMissing<IBoisson>(boissons, this.vente?.boisson)))
      .subscribe((boissons: IBoisson[]) => (this.boissonsSharedCollection = boissons));
  }
}
