import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { CompteCaisseFormService, CompteCaisseFormGroup } from './compte-caisse-form.service';
import { ICompteCaisse } from '../compte-caisse.model';
import { CompteCaisseService } from '../service/compte-caisse.service';
import { StatusCaisse } from 'app/entities/enumerations/status-caisse.model';

@Component({
  selector: 'jhi-compte-caisse-update',
  templateUrl: './compte-caisse-update.component.html',
})
export class CompteCaisseUpdateComponent implements OnInit {
  isSaving = false;
  compteCaisse: ICompteCaisse | null = null;
  statusCaisseValues = Object.keys(StatusCaisse);

  editForm: CompteCaisseFormGroup = this.compteCaisseFormService.createCompteCaisseFormGroup();

  constructor(
    protected compteCaisseService: CompteCaisseService,
    protected compteCaisseFormService: CompteCaisseFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compteCaisse }) => {
      this.compteCaisse = compteCaisse;
      if (compteCaisse) {
        this.updateForm(compteCaisse);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const compteCaisse = this.compteCaisseFormService.getCompteCaisse(this.editForm);
    if (compteCaisse.id !== null) {
      this.subscribeToSaveResponse(this.compteCaisseService.update(compteCaisse));
    } else {
      this.subscribeToSaveResponse(this.compteCaisseService.create(compteCaisse));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompteCaisse>>): void {
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

  protected updateForm(compteCaisse: ICompteCaisse): void {
    this.compteCaisse = compteCaisse;
    this.compteCaisseFormService.resetForm(this.editForm, compteCaisse);
  }
}
