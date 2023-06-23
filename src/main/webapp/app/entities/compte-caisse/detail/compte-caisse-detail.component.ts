import { SharedService } from 'app/shared/shared.service';
import { CompteCaisseService } from 'app/entities/compte-caisse/service/compte-caisse.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICompteCaisse } from '../compte-caisse.model';
import { HttpResponse } from '@angular/common/http';
import { Observable, finalize } from 'rxjs';

@Component({
  selector: 'jhi-compte-caisse-detail',
  templateUrl: './compte-caisse-detail.component.html',
})
export class CompteCaisseDetailComponent implements OnInit {
  compteCaisse: ICompteCaisse | undefined;
  montant?:number;

  constructor(protected activatedRoute: ActivatedRoute,private compteCaisseService: CompteCaisseService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compteCaisse }) => {
      this.compteCaisse = compteCaisse;
    });
  }

  update(type:string): void {

    if(type==='INJECTION'){

      this.subscribeToSaveResponse(this.compteCaisseService.updateInjection(this.compteCaisse,this.montant!));

    }else if(type==='PRET'){
      
      this.subscribeToSaveResponse(this.compteCaisseService.updatePret(this.compteCaisse,this.montant!));

    }else if(type==='CONTROL'){
      
      this.subscribeToSaveResponse(this.compteCaisseService.setControl(this.compteCaisse,this.montant!));

    }

    
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompteCaisse>>): void {
    result.subscribe({
      next: () => this.onSaveSuccess(),
      error: (error) => this.onSaveError(error),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
    SharedService.loadCaisse.next(true)
  }

  protected onSaveError(error:any): void {

    console.log(error)
    // Api for inheritance.
  }

  previousState(): void {
    window.history.back();
  }
}
