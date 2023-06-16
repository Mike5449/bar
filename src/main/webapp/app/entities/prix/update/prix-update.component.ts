import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PrixFormService, PrixFormGroup } from './prix-form.service';
import { IPrix } from '../prix.model';
import { PrixService } from '../service/prix.service';
import { IBoisson } from 'app/entities/boisson/boisson.model';
import { BoissonService } from 'app/entities/boisson/service/boisson.service';

@Component({
  selector: 'jhi-prix-update',
  templateUrl: './prix-update.component.html',
})
export class PrixUpdateComponent implements OnInit {
  isSaving = false;
  prix: IPrix | null = null;

  boissonsSharedCollection: IBoisson[] = [];

  editForm: PrixFormGroup = this.prixFormService.createPrixFormGroup();

  constructor(
    protected prixService: PrixService,
    protected prixFormService: PrixFormService,
    protected boissonService: BoissonService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareBoisson = (o1: IBoisson | null, o2: IBoisson | null): boolean => this.boissonService.compareBoisson(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prix }) => {
      this.prix = prix;
      if (prix) {
        this.updateForm(prix);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const prix = this.prixFormService.getPrix(this.editForm);
    if (prix.id !== null) {
      this.subscribeToSaveResponse(this.prixService.update(prix));
    } else {
      this.subscribeToSaveResponse(this.prixService.create(prix));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrix>>): void {
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

  protected updateForm(prix: IPrix): void {
    this.prix = prix;
    this.prixFormService.resetForm(this.editForm, prix);

    this.boissonsSharedCollection = this.boissonService.addBoissonToCollectionIfMissing<IBoisson>(
      this.boissonsSharedCollection,
      prix.boisson
    );
  }

  protected loadRelationshipsOptions(): void {
    this.boissonService
      .query()
      .pipe(map((res: HttpResponse<IBoisson[]>) => res.body ?? []))
      .pipe(map((boissons: IBoisson[]) => this.boissonService.addBoissonToCollectionIfMissing<IBoisson>(boissons, this.prix?.boisson)))
      .subscribe((boissons: IBoisson[]) => (this.boissonsSharedCollection = boissons));
  }
}
