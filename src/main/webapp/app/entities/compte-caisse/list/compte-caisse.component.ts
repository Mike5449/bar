import { Component, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICompteCaisse } from '../compte-caisse.model';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, CompteCaisseService } from '../service/compte-caisse.service';
import { CompteCaisseDeleteDialogComponent } from '../delete/compte-caisse-delete-dialog.component';
import { StatusCaisse } from 'app/entities/enumerations/status-caisse.model';

@Component({
  selector: 'jhi-compte-caisse',
  templateUrl: './compte-caisse.component.html',
})
export class CompteCaisseComponent implements OnInit {
  compteCaisses?: ICompteCaisse[];
  isLoading = false;

  predicate = 'id';
  ascending = true;

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  idCaissier?:number;
  constructor(
    protected compteCaisseService: CompteCaisseService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected modalService: NgbModal
  ) {}

  trackId = (_index: number, item: ICompteCaisse): number => this.compteCaisseService.getCompteCaisseIdentifier(item);

  ngOnInit(): void {
    this.load();
  }

  delete(compteCaisse: ICompteCaisse): void {
    const modalRef = this.modalService.open(CompteCaisseDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.compteCaisse = compteCaisse;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        switchMap(() => this.loadFromBackendWithRouteInformations())
      )
      .subscribe({
        next: (res: EntityArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      });
  }

  // groupeByEmployeeAnDCient(compte: ICompteCaisse[]) {
  //   const tableauGrouper:any = [];

  //   // Parcourir chaque objet de bilan
  //   compte.forEach(obj => {
  //     const { status , employee } = obj;
     
  //     // Vérifier si un objet similaire a déjà été ajouté à tableauGrouper
  //     const userAccountIndex = tableauGrouper.findIndex((user:any) => user.status === status);
    
  //     if (userAccountIndex === -1) {
  //       // Si l'utilisateur n'a pas encore été ajouté, créer un nouvel objet
  //       tableauGrouper.push({
  //         status,
  //          employees: [
  //           {
  //              employee,
  //             product: [obj]
  //           }
  //         ]
  //       });
  //     } else {
  //       // Sinon, ajouter l'objet à la liste d'productrmations de l'utilisateur existant
  //       const user = tableauGrouper[userAccountIndex]. employees.find((u:any) => u. employee?.id ===  employee?.id);
  //       if (user) {
  //         user.product.push(obj);
  //       } else {
  //         tableauGrouper[userAccountIndex]. employees.push({
  //            employee,
  //           product: [obj]
  //         });
  //       }
  //     }
  //   });

  //   // this.bilanAPayerService=tableauGrouper
    
  //     console.log(tableauGrouper);
    
  //   // this.salesGroupe = tableauGrouper;



  // }

  openOrClose(idCaissier?:number){

    if(this.idCaissier===idCaissier){

      this.idCaissier=undefined;

    }else{
      this.idCaissier=idCaissier;

    }


  }

  onlyActive(compteCaisse:ICompteCaisse[]):ICompteCaisse[]{

    return compteCaisse.filter(data=>data.status===StatusCaisse.ACTIVE);

  }

  onlyInactive(compteCaisse:ICompteCaisse[] , idEmploye?:number):ICompteCaisse[]{

    return compteCaisse.filter(data=>data.status===StatusCaisse.INACTIVE && data.employee?.id===idEmploye);

  }

  load(): void {
    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(): void {
    this.handleNavigation(this.page, this.predicate, this.ascending);
  }

  navigateToPage(page = this.page): void {
    this.handleNavigation(page, this.predicate, this.ascending);
  }

  protected loadFromBackendWithRouteInformations(): Observable<EntityArrayResponseType> {
    return combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
      tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
      switchMap(() => this.queryBackend(this.page, this.predicate, this.ascending))
    );
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
    this.predicate = sort[0];
    this.ascending = sort[1] === ASC;
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.compteCaisses = dataFromBody;
    // this.groupeByEmployeeAnDCient(this.compteCaisses)
  }

  protected fillComponentAttributesFromResponseBody(data: ICompteCaisse[] | null): ICompteCaisse[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(page?: number, predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const pageToLoad: number = page ?? 1;
    const queryObject = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };
    return this.compteCaisseService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page = this.page, predicate?: string, ascending?: boolean): void {
    const queryParamsObj = {
      page,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }
}
