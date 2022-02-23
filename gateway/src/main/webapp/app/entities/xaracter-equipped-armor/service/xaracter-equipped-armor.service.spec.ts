import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IXaracterEquippedArmor, XaracterEquippedArmor } from '../xaracter-equipped-armor.model';

import { XaracterEquippedArmorService } from './xaracter-equipped-armor.service';

describe('XaracterEquippedArmor Service', () => {
  let service: XaracterEquippedArmorService;
  let httpMock: HttpTestingController;
  let elemDefault: IXaracterEquippedArmor;
  let expectedResult: IXaracterEquippedArmor | IXaracterEquippedArmor[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(XaracterEquippedArmorService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      xaracterId: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a XaracterEquippedArmor', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new XaracterEquippedArmor()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a XaracterEquippedArmor', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          xaracterId: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a XaracterEquippedArmor', () => {
      const patchObject = Object.assign(
        {
          xaracterId: 1,
        },
        new XaracterEquippedArmor()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of XaracterEquippedArmor', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          xaracterId: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a XaracterEquippedArmor', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addXaracterEquippedArmorToCollectionIfMissing', () => {
      it('should add a XaracterEquippedArmor to an empty array', () => {
        const xaracterEquippedArmor: IXaracterEquippedArmor = { id: 123 };
        expectedResult = service.addXaracterEquippedArmorToCollectionIfMissing([], xaracterEquippedArmor);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(xaracterEquippedArmor);
      });

      it('should not add a XaracterEquippedArmor to an array that contains it', () => {
        const xaracterEquippedArmor: IXaracterEquippedArmor = { id: 123 };
        const xaracterEquippedArmorCollection: IXaracterEquippedArmor[] = [
          {
            ...xaracterEquippedArmor,
          },
          { id: 456 },
        ];
        expectedResult = service.addXaracterEquippedArmorToCollectionIfMissing(xaracterEquippedArmorCollection, xaracterEquippedArmor);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a XaracterEquippedArmor to an array that doesn't contain it", () => {
        const xaracterEquippedArmor: IXaracterEquippedArmor = { id: 123 };
        const xaracterEquippedArmorCollection: IXaracterEquippedArmor[] = [{ id: 456 }];
        expectedResult = service.addXaracterEquippedArmorToCollectionIfMissing(xaracterEquippedArmorCollection, xaracterEquippedArmor);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(xaracterEquippedArmor);
      });

      it('should add only unique XaracterEquippedArmor to an array', () => {
        const xaracterEquippedArmorArray: IXaracterEquippedArmor[] = [{ id: 123 }, { id: 456 }, { id: 92417 }];
        const xaracterEquippedArmorCollection: IXaracterEquippedArmor[] = [{ id: 123 }];
        expectedResult = service.addXaracterEquippedArmorToCollectionIfMissing(
          xaracterEquippedArmorCollection,
          ...xaracterEquippedArmorArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const xaracterEquippedArmor: IXaracterEquippedArmor = { id: 123 };
        const xaracterEquippedArmor2: IXaracterEquippedArmor = { id: 456 };
        expectedResult = service.addXaracterEquippedArmorToCollectionIfMissing([], xaracterEquippedArmor, xaracterEquippedArmor2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(xaracterEquippedArmor);
        expect(expectedResult).toContain(xaracterEquippedArmor2);
      });

      it('should accept null and undefined values', () => {
        const xaracterEquippedArmor: IXaracterEquippedArmor = { id: 123 };
        expectedResult = service.addXaracterEquippedArmorToCollectionIfMissing([], null, xaracterEquippedArmor, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(xaracterEquippedArmor);
      });

      it('should return initial array if no XaracterEquippedArmor is added', () => {
        const xaracterEquippedArmorCollection: IXaracterEquippedArmor[] = [{ id: 123 }];
        expectedResult = service.addXaracterEquippedArmorToCollectionIfMissing(xaracterEquippedArmorCollection, undefined, null);
        expect(expectedResult).toEqual(xaracterEquippedArmorCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
