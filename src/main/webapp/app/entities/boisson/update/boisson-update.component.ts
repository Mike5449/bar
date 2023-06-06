import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { BoissonFormService, BoissonFormGroup } from './boisson-form.service';
import { IBoisson } from '../boisson.model';
import { BoissonService } from '../service/boisson.service';
import { Categorie } from 'app/entities/enumerations/categorie.model';

@Component({
  selector: 'jhi-boisson-update',
  templateUrl: './boisson-update.component.html',
})
export class BoissonUpdateComponent implements OnInit {
  isSaving = false;
  boisson: IBoisson | null = null;
  categorieValues = Object.keys(Categorie);

  editForm: BoissonFormGroup = this.boissonFormService.createBoissonFormGroup();

  constructor(
    protected boissonService: BoissonService,
    protected boissonFormService: BoissonFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ boisson }) => {
      this.boisson = boisson;
      if (boisson) {
        this.updateForm(boisson);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const boisson = this.boissonFormService.getBoisson(this.editForm);
    if (boisson.id !== null) {
      this.subscribeToSaveResponse(this.boissonService.update(boisson));
    } else {
      this.subscribeToSaveResponse(this.boissonService.create(boisson));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBoisson>>): void {
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

  protected updateForm(boisson: IBoisson): void {
    this.boisson = boisson;
    this.boissonFormService.resetForm(this.editForm, boisson);
  }
}
