import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBoisson } from '../boisson.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../boisson.test-samples';

import { BoissonService } from './boisson.service';

const requireRestSample: IBoisson = {
  ...sampleWithRequiredData,
};

describe('Boisson Service', () => {
  let service: BoissonService;
  let httpMock: HttpTestingController;
  let expectedResult: IBoisson | IBoisson[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BoissonService);
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

    it('should create a Boisson', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const boisson = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(boisson).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Boisson', () => {
      const boisson = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(boisson).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Boisson', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Boisson', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Boisson', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBoissonToCollectionIfMissing', () => {
      it('should add a Boisson to an empty array', () => {
        const boisson: IBoisson = sampleWithRequiredData;
        expectedResult = service.addBoissonToCollectionIfMissing([], boisson);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(boisson);
      });

      it('should not add a Boisson to an array that contains it', () => {
        const boisson: IBoisson = sampleWithRequiredData;
        const boissonCollection: IBoisson[] = [
          {
            ...boisson,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBoissonToCollectionIfMissing(boissonCollection, boisson);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Boisson to an array that doesn't contain it", () => {
        const boisson: IBoisson = sampleWithRequiredData;
        const boissonCollection: IBoisson[] = [sampleWithPartialData];
        expectedResult = service.addBoissonToCollectionIfMissing(boissonCollection, boisson);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(boisson);
      });

      it('should add only unique Boisson to an array', () => {
        const boissonArray: IBoisson[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const boissonCollection: IBoisson[] = [sampleWithRequiredData];
        expectedResult = service.addBoissonToCollectionIfMissing(boissonCollection, ...boissonArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const boisson: IBoisson = sampleWithRequiredData;
        const boisson2: IBoisson = sampleWithPartialData;
        expectedResult = service.addBoissonToCollectionIfMissing([], boisson, boisson2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(boisson);
        expect(expectedResult).toContain(boisson2);
      });

      it('should accept null and undefined values', () => {
        const boisson: IBoisson = sampleWithRequiredData;
        expectedResult = service.addBoissonToCollectionIfMissing([], null, boisson, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(boisson);
      });

      it('should return initial array if no Boisson is added', () => {
        const boissonCollection: IBoisson[] = [sampleWithRequiredData];
        expectedResult = service.addBoissonToCollectionIfMissing(boissonCollection, undefined, null);
        expect(expectedResult).toEqual(boissonCollection);
      });
    });

    describe('compareBoisson', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBoisson(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBoisson(entity1, entity2);
        const compareResult2 = service.compareBoisson(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBoisson(entity1, entity2);
        const compareResult2 = service.compareBoisson(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBoisson(entity1, entity2);
        const compareResult2 = service.compareBoisson(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
