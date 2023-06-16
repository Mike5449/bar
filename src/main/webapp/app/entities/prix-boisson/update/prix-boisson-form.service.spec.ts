import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../prix-boisson.test-samples';

import { PrixBoissonFormService } from './prix-boisson-form.service';

describe('PrixBoisson Form Service', () => {
  let service: PrixBoissonFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PrixBoissonFormService);
  });

  describe('Service methods', () => {
    describe('createPrixBoissonFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPrixBoissonFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            prix: expect.any(Object),
            boisson: expect.any(Object),
          })
        );
      });

      it('passing IPrixBoisson should create a new form with FormGroup', () => {
        const formGroup = service.createPrixBoissonFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            prix: expect.any(Object),
            boisson: expect.any(Object),
          })
        );
      });
    });

    describe('getPrixBoisson', () => {
      it('should return NewPrixBoisson for default PrixBoisson initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPrixBoissonFormGroup(sampleWithNewData);

        const prixBoisson = service.getPrixBoisson(formGroup) as any;

        expect(prixBoisson).toMatchObject(sampleWithNewData);
      });

      it('should return NewPrixBoisson for empty PrixBoisson initial value', () => {
        const formGroup = service.createPrixBoissonFormGroup();

        const prixBoisson = service.getPrixBoisson(formGroup) as any;

        expect(prixBoisson).toMatchObject({});
      });

      it('should return IPrixBoisson', () => {
        const formGroup = service.createPrixBoissonFormGroup(sampleWithRequiredData);

        const prixBoisson = service.getPrixBoisson(formGroup) as any;

        expect(prixBoisson).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPrixBoisson should not enable id FormControl', () => {
        const formGroup = service.createPrixBoissonFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPrixBoisson should disable id FormControl', () => {
        const formGroup = service.createPrixBoissonFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
