import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpHeaders, HttpRequest, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, finalize, Observable, switchMap, tap } from 'rxjs';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISale } from '../sale.model';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, SaleService } from '../service/sale.service';
import { SaleDeleteDialogComponent } from '../delete/sale-delete-dialog.component';
import { IEmployee } from 'app/entities/employee/employee.model';
import { TreeViewComponent } from '@syncfusion/ej2-angular-navigations';
import { IClient } from 'app/entities/client/client.model';
import { SharedService } from 'app/shared/shared.service';
import { StatusVente } from 'app/entities/enumerations/status-vente.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { ClientFormGroup, ClientFormService } from 'app/entities/client/update/client-form.service';
import { DepotService } from 'app/entities/depot/service/depot.service';
import { IDepot } from 'app/entities/depot/depot.model';
import { TokenDecodeService } from 'app/shared/token-decode.service';
export enum TypeAction {
  INSERT,
  UPDATE,
  DETAIL,
}
@Component({
  selector: 'jhi-sale',
  templateUrl: './sale.component.html',
  styleUrls:['./sale.component.scss']
})
// export interface ISaleGroupe{
//   employee?:Pick<IEmployee, 'id' | 'firstName'> | null;
//   client?:
// }
// export interface IClient{
//   client?:Pick<IClient, 'client'> | null;

// }
export class SaleComponent implements OnInit {
  sales?: ISale[];
  salesGroupe?: any[];
  clients?:IClient[]=[];
  isLoading = false;

  predicate = 'id';
  ascending = true;

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  isLine:any=0

  type: TypeAction = TypeAction.INSERT;
  content:any;
  closeResult = '';
  saleSelected:ISale | null=null;
  isTransaction = false;

  currentClient:IClient | undefined;
  isNewClient?:boolean;
  employeeSearching:string='';
  clientSearching:string='';
  clientName:string='';
  amountDepot?:number;
  infoToken?:any;
  


  editForm: ClientFormGroup = this.clientFormService.createClientFormGroup();

  constructor(
    protected saleService: SaleService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected modalService: NgbModal,protected clientService: ClientService,
    protected clientFormService: ClientFormService,
    protected depotService: DepotService,
    public tokenDecodeService: TokenDecodeService,

  ) {}


  trackId = (_index: number, item: ISale): number => this.saleService.getSaleIdentifier(item);

  ngOnInit(): void {
    this.infoToken=this.tokenDecodeService.getTokenInformation();
    console.log(this.infoToken)
    this.load();
    SharedService.mapModalVisible.subscribe(data => {
      if(!data){
        this.load();
        SharedService.mapModalVisible.next(true);
      }
    });
    
  }

  newClient(content:any){

    const client:any={id:0};
    client.name=this.clientName;
    client.id=null;
   
    if (client.id !== null) {
      this.subscribeToSaveClient(this.clientService.update(client));
    } else {
      this.subscribeToSaveClient(this.clientService.create(client));
    }

    this.type=TypeAction.INSERT;
    this.content=content;
    this.isNewClient=true;

  }
  newDepot(client:IClient){

    const depot:any={id:0};
    // depot.client=client;
    const employee:IEmployee={id:3,firstName:'multi-byte Administrateur'}
    depot.employee=employee;
    depot.amount=this.amountDepot;
    depot.id=null

    if (depot.id !== null) {
      this.subscribeToSaveDepot(this.depotService.update(depot));
    } else {
      this.subscribeToSaveDepot(this.depotService.create(depot));
    }

  }

  protected subscribeToSaveDepot(result: Observable<HttpResponse<IClient>>): void {
    result.subscribe({
      next: (data) => this.onSaveSuccessDepot(),
      // error: () => this.onSaveError(),
    });
  }

  protected subscribeToSaveClient(result: Observable<HttpResponse<IClient>>): void {
    result.subscribe({
      next: (data) => this.onSaveSuccessClient(data.body),
      // error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccessClient(client:any): void {
    this.newDepot(client)
    // SharedService.mapModalVisible.next(true);
    
  }
  protected onSaveSuccessDepot(): void {
   
    this.openForNewClient(this.content,this.type,true);
    
  }
  setEmployee (employee:string){
    this.employeeSearching=employee;
  }

  getClient(sale:ISale[]){

    const clienMap=new Map<number,IClient>()

    sale.forEach(data=>{
      if(data.client)
      clienMap.set(data.client.id,data.client);
    })

    if(this.clients){

      this.clients=Array.from<IClient>(clienMap.values())

    }
    console.log(this.clients)

  }

  getTypeAction(): typeof TypeAction {
    return TypeAction;
  }
  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

  open(content: any) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then(
      (result:any) => {
        this.closeResult = `Closed with: ${result}`;
      },
      reason => {
        this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
      }
    );
  }

  openForNewClient(content: any, type: TypeAction ,isNewClient:boolean) {

    this.type = type;
    this.isNewClient=isNewClient;
    // if (sale){this.saleSelected = sale;}
    this.open(content);
    this.isTransaction = false;
  }

  openSpecific(content: any, type: TypeAction ,currentClient?:IClient) {

    console.log(currentClient)

    this.currentClient=currentClient;
    this.type = type;
    // if (sale){this.saleSelected = sale;}
    this.open(content);
    this.isTransaction = false;
  }

  delete(sale: ISale): void {
    const modalRef = this.modalService.open(SaleDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.sale = sale;
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

  groupeByEmployeeAnDCient(sale: ISale[]) {
    const tableauGrouper:any = [];

    // Parcourir chaque objet de bilan
    sale.forEach(obj => {
      const { employee ,client } = obj;
     
      // Vérifier si un objet similaire a déjà été ajouté à tableauGrouper
      const userAccountIndex = tableauGrouper.findIndex((user:any) => user?.employee?.firstName === employee?.firstName);
    
      if (userAccountIndex === -1) {
        // Si l'utilisateur n'a pas encore été ajouté, créer un nouvel objet
        tableauGrouper.push({
          employee,
          clients: [
            {
              client,
              produit: [obj]
            }
          ]
        });
      } else {
        // Sinon, ajouter l'objet à la liste d'produitrmations de l'utilisateur existant
        const user = tableauGrouper[userAccountIndex].clients.find((u:any) => u.client?.name === client?.name);
        if (user) {
          user.produit.push(obj);
        } else {
          tableauGrouper[userAccountIndex].clients.push({
            client,
            produit: [obj]
          });
        }
      }
    });

    // this.bilanAPayerService=tableauGrouper
    
    // console.log(tableauGrouper);
    
    this.salesGroupe = tableauGrouper;



  }

  openLine(id: number) {



    if (!this.isLine) {

      this.isLine = id;


    } else {
      this.isLine = '';

    }


  }

  protected onSaveSuccess(): void {
    SharedService.mapModalVisible.next(false);
    
  }

protected onSaveError(message:any): void {
  const errorMessage:string=message?.error?.detail?.replace('500 INTERNAL_SERVER_ERROR','')

  // this.showDanger(errorMessage);
  // Api for inheritance.
}

validateSale(currentsale:ISale): void {
  // this.isSaving = true;
  const sale = currentsale;
  sale.status=StatusVente.VALIDATE;

  this.subscribeToValidatedateSale(this.saleService.update(sale));
  
}

  subscribeToValidatedateSale(result:Observable<HttpResponse<ISale>>):void{

    result.subscribe({
      next: () => this.onSaveSuccess(),
      error: (message) => this.onSaveError(message),
    });

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
    this.sales = dataFromBody;
    this.groupeByEmployeeAnDCient(this.sales);
    this.getClient(this.sales);
  }

  protected fillComponentAttributesFromResponseBody(data: ISale[] | null): ISale[] {
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
      eagerload: true,
      sort: this.getSortQueryParam(predicate, ascending),
    };
    return this.saleService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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