import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../salary.test-samples';

import { SalaryFormService } from './salary-form.service';

describe('Salary Form Service', () => {
  let service: SalaryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SalaryFormService);
  });

  describe('Service methods', () => {
    describe('createSalaryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSalaryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            month: expect.any(Object),
            year: expect.any(Object),
            amount: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });

      it('passing ISalary should create a new form with FormGroup', () => {
        const formGroup = service.createSalaryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            month: expect.any(Object),
            year: expect.any(Object),
            amount: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });
    });

    describe('getSalary', () => {
      it('should return NewSalary for default Salary initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSalaryFormGroup(sampleWithNewData);

        const salary = service.getSalary(formGroup) as any;

        expect(salary).toMatchObject(sampleWithNewData);
      });

      it('should return NewSalary for empty Salary initial value', () => {
        const formGroup = service.createSalaryFormGroup();

        const salary = service.getSalary(formGroup) as any;

        expect(salary).toMatchObject({});
      });

      it('should return ISalary', () => {
        const formGroup = service.createSalaryFormGroup(sampleWithRequiredData);

        const salary = service.getSalary(formGroup) as any;

        expect(salary).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISalary should not enable id FormControl', () => {
        const formGroup = service.createSalaryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSalary should disable id FormControl', () => {
        const formGroup = service.createSalaryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
