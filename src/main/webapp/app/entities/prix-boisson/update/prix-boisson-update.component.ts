import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PrixBoissonFormService, PrixBoissonFormGroup } from './prix-boisson-form.service';
import { IPrixBoisson } from '../prix-boisson.model';
import { PrixBoissonService } from '../service/prix-boisson.service';
import { IBoisson } from 'app/entities/boisson/boisson.model';
import { BoissonService } from 'app/entities/boisson/service/boisson.service';

@Component({
  selector: 'jhi-prix-boisson-update',
  templateUrl: './prix-boisson-update.component.html',
})
export class PrixBoissonUpdateComponent implements OnInit {
  isSaving = false;
  prixBoisson: IPrixBoisson | null = null;

  boissonsSharedCollection: IBoisson[] = [];

  editForm: PrixBoissonFormGroup = this.prixBoissonFormService.createPrixBoissonFormGroup();

  constructor(
    protected prixBoissonService: PrixBoissonService,
    protected prixBoissonFormService: PrixBoissonFormService,
    protected boissonService: BoissonService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareBoisson = (o1: IBoisson | null, o2: IBoisson | null): boolean => this.boissonService.compareBoisson(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prixBoisson }) => {
      this.prixBoisson = prixBoisson;
      if (prixBoisson) {
        this.updateForm(prixBoisson);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const prixBoisson = this.prixBoissonFormService.getPrixBoisson(this.editForm);
    if (prixBoisson.id !== null) {
      this.subscribeToSaveResponse(this.prixBoissonService.update(prixBoisson));
    } else {
      this.subscribeToSaveResponse(this.prixBoissonService.create(prixBoisson));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrixBoisson>>): void {
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

  protected updateForm(prixBoisson: IPrixBoisson): void {
    this.prixBoisson = prixBoisson;
    this.prixBoissonFormService.resetForm(this.editForm, prixBoisson);

    this.boissonsSharedCollection = this.boissonService.addBoissonToCollectionIfMissing<IBoisson>(
      this.boissonsSharedCollection,
      prixBoisson.boisson
    );
  }

  protected loadRelationshipsOptions(): void {
    this.boissonService
      .query()
      .pipe(map((res: HttpResponse<IBoisson[]>) => res.body ?? []))
      .pipe(
        map((boissons: IBoisson[]) => this.boissonService.addBoissonToCollectionIfMissing<IBoisson>(boissons, this.prixBoisson?.boisson))
      )
      .subscribe((boissons: IBoisson[]) => (this.boissonsSharedCollection = boissons));
  }
}
