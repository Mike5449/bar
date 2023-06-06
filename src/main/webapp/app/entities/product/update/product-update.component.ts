import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ProductFormService, ProductFormGroup } from './product-form.service';
import { IProduct } from '../product.model';
import { ProductService } from '../service/product.service';
import { Categorie } from 'app/entities/enumerations/categorie.model';
import { Section } from 'app/entities/enumerations/section.model';

@Component({
  selector: 'jhi-product-update',
  templateUrl: './product-update.component.html',
})
export class ProductUpdateComponent implements OnInit {
  isSaving = false;
  product: IProduct | null = null;
  categorieValues = Object.keys(Categorie);
  sectionValues = Object.keys(Section);

  imageURL:string | ArrayBuffer | null | undefined;
  imagBase64String?:string;

  editForm: ProductFormGroup = this.productFormService.createProductFormGroup();

  constructor(
    protected productService: ProductService,
    protected productFormService: ProductFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      this.product = product;
      if (product) {
        this.updateForm(product);
      }
    });
  }

  onFileSelected(envent:any){
    const file:File=envent.target.files[0];

    this.previewImage(file)
    this.convertToBase64(file).then((base64String:string)=>{
      this.imagBase64String=base64String
    })
    
  }
  previewImage(file:File){
    const reader=new FileReader();
    reader.readAsDataURL(file);
    reader.onload=()=>{
      this.imageURL=reader.result;
      
    }
  }
  convertToBase64(file:File):Promise<string>{
    return new Promise((resolve,reject)=>{
      const reader =new FileReader();
      reader.readAsDataURL(file);
      reader.onload=()=>{
        const base64String=reader.result?.toString();
        if(base64String)
        resolve(base64String);
        

      };
      reader.onerror=(error)=>reject(error);

    })
  }
  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const product = this.productFormService.getProduct(this.editForm);
    product.image=this.imagBase64String;
    // console.log(product)
    if (product.id !== null) {
      this.subscribeToSaveResponse(this.productService.update(product));
    } else {
      this.subscribeToSaveResponse(this.productService.create(product));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduct>>): void {
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

  protected updateForm(product: IProduct): void {
    this.product = product;
    this.productFormService.resetForm(this.editForm, product);
  }
}
