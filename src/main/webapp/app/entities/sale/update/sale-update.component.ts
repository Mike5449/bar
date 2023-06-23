import { ISale } from './../sale.model';
import { Component, Input, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SaleFormService, SaleFormGroup } from './sale-form.service';

import { SaleService } from '../service/sale.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { IDepot } from 'app/entities/depot/depot.model';
import { DepotService } from 'app/entities/depot/service/depot.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IProductPrice } from 'app/entities/product-price/product-price.model';
import { ProductPriceService } from 'app/entities/product-price/service/product-price.service';
import { StatusVente } from 'app/entities/enumerations/status-vente.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SharedService } from 'app/shared/shared.service';
import { DomSanitizer } from '@angular/platform-browser';
import { StockService } from 'app/entities/stock/service/stock.service';
import { IStock } from 'app/entities/stock/stock.model';

interface IProductWithPrice{
  name:string;
  price:number;
}

@Component({
  selector: 'jhi-sale-update',
  templateUrl: './sale-update.component.html',
})
export class SaleUpdateComponent implements OnInit {
  @Input()currentClient:IClient | undefined;
  @Input()currentEmployee:IEmployee | undefined;
  isSaving = false;
  sale: ISale | null = null;
  statusVenteValues = Object.keys(StatusVente);

  employeesSharedCollection: IEmployee[] = [];
  clientsSharedCollection: IClient[] = [];
  depotsSharedCollection: IDepot[] = [];
  productsSharedCollection: IProduct[] = [];
  productPricesSharedCollection: IProductPrice[] = [];

  editForm: SaleFormGroup = this.saleFormService.createSaleFormGroup();

  saleOnClient?:ISale={id:0};

  productPrices:(number | undefined)[]=[];

  constructor(
    protected saleService: SaleService,
    protected saleFormService: SaleFormService,
    protected employeeService: EmployeeService,
    protected clientService: ClientService,
    protected depotService: DepotService,
    protected productService: ProductService,
    protected productPriceService: ProductPriceService,
    protected activatedRoute: ActivatedRoute,
    protected modalService: NgbModal,
    private sanitizer:DomSanitizer,
    protected stockService: StockService,
    
  ) {}

  setBlob(path:any):any{

    const blob=new Blob([path],{type:'image/*'})

    return this.sanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(blob));

  }

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareClient = (o1: IClient | null, o2: IClient | null): boolean => this.clientService.compareClient(o1, o2);

  compareDepot = (o1: IDepot | null, o2: IDepot | null): boolean => this.depotService.compareDepot(o1, o2);

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareProductPrice = (o1: IProductPrice | null, o2: IProductPrice | null): boolean =>
    this.productPriceService.compareProductPrice(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sale }) => {
      this.sale = sale;
      if (sale) {
        this.updateForm(sale);
      }

      this.loadRelationshipsOptions();
    });
    

    if(this.currentClient){

      const sale: ISale = {id:0};

        sale.amountTotal=0
        sale.client=this.currentClient;
        sale.depot=null;
        if(this.currentEmployee){

         sale.employee=this.currentEmployee;

        }else{

          sale.employee=null;

        }
        sale.product=null;
        sale.quantity=0;
        sale.status=StatusVente.NEW;
        sale.unitPrice=0;
        this.updateForm(sale);

    }
    else{
      
    }
  }

  getImageDataUrl(img:any,contentType:any){
    return `data:${contentType};base64,${img}`

  }

  previousState(): void {
    window.history.back();
  }

  submitPrices(){

    const productsWithQuantity=this.productsSharedCollection.filter((product,index)=>{

      const quantity=this.productPrices[index];
      return quantity && quantity>0

      

    }).map((product,index)=>{

      const quantity=this.productPrices[index]
      return { ...product,quantity}
    })
    console.log(productsWithQuantity)
    productsWithQuantity.forEach(data=>{

      console.log(data)


      const sale: any = {id:0};

        sale.id=null;
        sale.amountTotal=0
        sale.client=this.currentClient;
        sale.depot=null;
        sale.product=data;
        sale.quantity=data.quantity;
        sale.status=StatusVente.NEW;
        sale.unitPrice=0;
      
        if(this.currentClient){

          sale.employee=this.currentEmployee;

          
        }else{

          sale.employee=null;

        }
        
        if (sale.id !== null) {
          this.subscribeToSaveResponse(this.saleService.update(sale));
          
        } else {
          this.subscribeToSaveResponse(this.saleService.create(sale));
          
        }

    })
    
  }


  save(): void {
    this.isSaving = true;
    const sale = this.saleFormService.getSale(this.editForm);
    console.log(sale)

    if(this.currentClient){

      sale.id=null;
    }
    
    if (sale.id !== null) {
      this.subscribeToSaveResponse(this.saleService.update(sale));
      
    } else {
      this.subscribeToSaveResponse(this.saleService.create(sale));
      
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISale>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: (data) => this.onSaveSuccess(data),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(data:any): void {
    // this.previousState();
    console.log(data)
    SharedService.loadCaisse.next(true);
    SharedService.mapModalVisible.next(false);
    this.modalService.dismissAll();
  }

  // updatStock(): void {
    
  //   this.subscribeToSaveStock(this.stockService.create(stock));
    
  // }

  // protected subscribeToSaveStock(result: Observable<HttpResponse<IStock>>): void {
  //   result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
  //     next: () => this.onSaveSuccess(),
  //     error: () => this.onSaveError(),
  //   });
  // }


  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(sale: ISale): void {
    this.sale = sale;
    this.saleFormService.resetForm(this.editForm, sale);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      sale.employee
    );
    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing<IClient>(this.clientsSharedCollection, sale.client);
    this.depotsSharedCollection = this.depotService.addDepotToCollectionIfMissing<IDepot>(this.depotsSharedCollection, sale.depot);
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing<IProduct>(
      this.productsSharedCollection,
      sale.product
    );
    this.productPricesSharedCollection = this.productPriceService.addProductPriceToCollectionIfMissing<IProductPrice>(
      this.productPricesSharedCollection,
      sale.currentPrice
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) => this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.sale?.employee))
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing<IClient>(clients, this.sale?.client)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));

    this.depotService
      .query()
      .pipe(map((res: HttpResponse<IDepot[]>) => res.body ?? []))
      .pipe(map((depots: IDepot[]) => this.depotService.addDepotToCollectionIfMissing<IDepot>(depots, this.sale?.depot)))
      .subscribe((depots: IDepot[]) => (this.depotsSharedCollection = depots));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.sale?.product)))
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));

    this.productPriceService
      .query()
      .pipe(map((res: HttpResponse<IProductPrice[]>) => res.body ?? []))
      .pipe(
        map((productPrices: IProductPrice[]) =>
          this.productPriceService.addProductPriceToCollectionIfMissing<IProductPrice>(productPrices, this.sale?.currentPrice)
        )
      )
      .subscribe((productPrices: IProductPrice[]) => (this.productPricesSharedCollection = productPrices));
  }
}
