import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../boisson.test-samples';

import { BoissonFormService } from './boisson-form.service';

describe('Boisson Form Service', () => {
  let service: BoissonFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BoissonFormService);
  });

  describe('Service methods', () => {
    describe('createBoissonFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBoissonFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            image: expect.any(Object),
            type: expect.any(Object),
          })
        );
      });

      it('passing IBoisson should create a new form with FormGroup', () => {
        const formGroup = service.createBoissonFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            image: expect.any(Object),
            type: expect.any(Object),
          })
        );
      });
    });

    describe('getBoisson', () => {
      it('should return NewBoisson for default Boisson initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createBoissonFormGroup(sampleWithNewData);

        const boisson = service.getBoisson(formGroup) as any;

        expect(boisson).toMatchObject(sampleWithNewData);
      });

      it('should return NewBoisson for empty Boisson initial value', () => {
        const formGroup = service.createBoissonFormGroup();

        const boisson = service.getBoisson(formGroup) as any;

        expect(boisson).toMatchObject({});
      });

      it('should return IBoisson', () => {
        const formGroup = service.createBoissonFormGroup(sampleWithRequiredData);

        const boisson = service.getBoisson(formGroup) as any;

        expect(boisson).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBoisson should not enable id FormControl', () => {
        const formGroup = service.createBoissonFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBoisson should disable id FormControl', () => {
        const formGroup = service.createBoissonFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
