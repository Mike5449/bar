import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../product-price.test-samples';

import { ProductPriceFormService } from './product-price-form.service';

describe('ProductPrice Form Service', () => {
  let service: ProductPriceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductPriceFormService);
  });

  describe('Service methods', () => {
    describe('createProductPriceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductPriceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            price: expect.any(Object),
            status: expect.any(Object),
            produit: expect.any(Object),
          })
        );
      });

      it('passing IProductPrice should create a new form with FormGroup', () => {
        const formGroup = service.createProductPriceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            price: expect.any(Object),
            status: expect.any(Object),
            produit: expect.any(Object),
          })
        );
      });
    });

    describe('getProductPrice', () => {
      it('should return NewProductPrice for default ProductPrice initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createProductPriceFormGroup(sampleWithNewData);

        const productPrice = service.getProductPrice(formGroup) as any;

        expect(productPrice).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductPrice for empty ProductPrice initial value', () => {
        const formGroup = service.createProductPriceFormGroup();

        const productPrice = service.getProductPrice(formGroup) as any;

        expect(productPrice).toMatchObject({});
      });

      it('should return IProductPrice', () => {
        const formGroup = service.createProductPriceFormGroup(sampleWithRequiredData);

        const productPrice = service.getProductPrice(formGroup) as any;

        expect(productPrice).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductPrice should not enable id FormControl', () => {
        const formGroup = service.createProductPriceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductPrice should disable id FormControl', () => {
        const formGroup = service.createProductPriceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
