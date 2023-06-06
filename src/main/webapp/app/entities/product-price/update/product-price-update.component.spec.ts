import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProductPriceFormService } from './product-price-form.service';
import { ProductPriceService } from '../service/product-price.service';
import { IProductPrice } from '../product-price.model';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

import { ProductPriceUpdateComponent } from './product-price-update.component';

describe('ProductPrice Management Update Component', () => {
  let comp: ProductPriceUpdateComponent;
  let fixture: ComponentFixture<ProductPriceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productPriceFormService: ProductPriceFormService;
  let productPriceService: ProductPriceService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProductPriceUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ProductPriceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductPriceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productPriceFormService = TestBed.inject(ProductPriceFormService);
    productPriceService = TestBed.inject(ProductPriceService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Product query and add missing value', () => {
      const productPrice: IProductPrice = { id: 456 };
      const produit: IProduct = { id: 52348 };
      productPrice.produit = produit;

      const productCollection: IProduct[] = [{ id: 74246 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [produit];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productPrice });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining)
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const productPrice: IProductPrice = { id: 456 };
      const produit: IProduct = { id: 90013 };
      productPrice.produit = produit;

      activatedRoute.data = of({ productPrice });
      comp.ngOnInit();

      expect(comp.productsSharedCollection).toContain(produit);
      expect(comp.productPrice).toEqual(productPrice);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductPrice>>();
      const productPrice = { id: 123 };
      jest.spyOn(productPriceFormService, 'getProductPrice').mockReturnValue(productPrice);
      jest.spyOn(productPriceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productPrice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productPrice }));
      saveSubject.complete();

      // THEN
      expect(productPriceFormService.getProductPrice).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productPriceService.update).toHaveBeenCalledWith(expect.objectContaining(productPrice));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductPrice>>();
      const productPrice = { id: 123 };
      jest.spyOn(productPriceFormService, 'getProductPrice').mockReturnValue({ id: null });
      jest.spyOn(productPriceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productPrice: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productPrice }));
      saveSubject.complete();

      // THEN
      expect(productPriceFormService.getProductPrice).toHaveBeenCalled();
      expect(productPriceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductPrice>>();
      const productPrice = { id: 123 };
      jest.spyOn(productPriceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productPrice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productPriceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProduct', () => {
      it('Should forward to productService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(productService, 'compareProduct');
        comp.compareProduct(entity, entity2);
        expect(productService.compareProduct).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
