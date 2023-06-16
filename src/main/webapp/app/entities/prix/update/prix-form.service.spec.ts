import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../prix.test-samples';

import { PrixFormService } from './prix-form.service';

describe('Prix Form Service', () => {
  let service: PrixFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PrixFormService);
  });

  describe('Service methods', () => {
    describe('createPrixFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPrixFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            prix: expect.any(Object),
            date: expect.any(Object),
            boisson: expect.any(Object),
          })
        );
      });

      it('passing IPrix should create a new form with FormGroup', () => {
        const formGroup = service.createPrixFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            prix: expect.any(Object),
            date: expect.any(Object),
            boisson: expect.any(Object),
          })
        );
      });
    });

    describe('getPrix', () => {
      it('should return NewPrix for default Prix initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPrixFormGroup(sampleWithNewData);

        const prix = service.getPrix(formGroup) as any;

        expect(prix).toMatchObject(sampleWithNewData);
      });

      it('should return NewPrix for empty Prix initial value', () => {
        const formGroup = service.createPrixFormGroup();

        const prix = service.getPrix(formGroup) as any;

        expect(prix).toMatchObject({});
      });

      it('should return IPrix', () => {
        const formGroup = service.createPrixFormGroup(sampleWithRequiredData);

        const prix = service.getPrix(formGroup) as any;

        expect(prix).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPrix should not enable id FormControl', () => {
        const formGroup = service.createPrixFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPrix should disable id FormControl', () => {
        const formGroup = service.createPrixFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
