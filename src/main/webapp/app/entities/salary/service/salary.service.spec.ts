import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISalary } from '../salary.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../salary.test-samples';

import { SalaryService } from './salary.service';

const requireRestSample: ISalary = {
  ...sampleWithRequiredData,
};

describe('Salary Service', () => {
  let service: SalaryService;
  let httpMock: HttpTestingController;
  let expectedResult: ISalary | ISalary[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SalaryService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Salary', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const salary = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(salary).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Salary', () => {
      const salary = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(salary).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Salary', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Salary', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Salary', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSalaryToCollectionIfMissing', () => {
      it('should add a Salary to an empty array', () => {
        const salary: ISalary = sampleWithRequiredData;
        expectedResult = service.addSalaryToCollectionIfMissing([], salary);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(salary);
      });

      it('should not add a Salary to an array that contains it', () => {
        const salary: ISalary = sampleWithRequiredData;
        const salaryCollection: ISalary[] = [
          {
            ...salary,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSalaryToCollectionIfMissing(salaryCollection, salary);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Salary to an array that doesn't contain it", () => {
        const salary: ISalary = sampleWithRequiredData;
        const salaryCollection: ISalary[] = [sampleWithPartialData];
        expectedResult = service.addSalaryToCollectionIfMissing(salaryCollection, salary);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(salary);
      });

      it('should add only unique Salary to an array', () => {
        const salaryArray: ISalary[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const salaryCollection: ISalary[] = [sampleWithRequiredData];
        expectedResult = service.addSalaryToCollectionIfMissing(salaryCollection, ...salaryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const salary: ISalary = sampleWithRequiredData;
        const salary2: ISalary = sampleWithPartialData;
        expectedResult = service.addSalaryToCollectionIfMissing([], salary, salary2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(salary);
        expect(expectedResult).toContain(salary2);
      });

      it('should accept null and undefined values', () => {
        const salary: ISalary = sampleWithRequiredData;
        expectedResult = service.addSalaryToCollectionIfMissing([], null, salary, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(salary);
      });

      it('should return initial array if no Salary is added', () => {
        const salaryCollection: ISalary[] = [sampleWithRequiredData];
        expectedResult = service.addSalaryToCollectionIfMissing(salaryCollection, undefined, null);
        expect(expectedResult).toEqual(salaryCollection);
      });
    });

    describe('compareSalary', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSalary(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSalary(entity1, entity2);
        const compareResult2 = service.compareSalary(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSalary(entity1, entity2);
        const compareResult2 = service.compareSalary(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSalary(entity1, entity2);
        const compareResult2 = service.compareSalary(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
