import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { AttributeName } from 'app/entities/enumerations/attribute-name.model';
import { IXaracterAttribute, XaracterAttribute } from '../xaracter-attribute.model';

import { XaracterAttributeService } from './xaracter-attribute.service';

describe('XaracterAttribute Service', () => {
  let service: XaracterAttributeService;
  let httpMock: HttpTestingController;
  let elemDefault: IXaracterAttribute;
  let expectedResult: IXaracterAttribute | IXaracterAttribute[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(XaracterAttributeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: AttributeName.ST,
      points: 0,
      attributeModifier: 0,
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

    it('should create a XaracterAttribute', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new XaracterAttribute()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a XaracterAttribute', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          points: 1,
          attributeModifier: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a XaracterAttribute', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          attributeModifier: 1,
        },
        new XaracterAttribute()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of XaracterAttribute', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          points: 1,
          attributeModifier: 1,
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

    it('should delete a XaracterAttribute', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addXaracterAttributeToCollectionIfMissing', () => {
      it('should add a XaracterAttribute to an empty array', () => {
        const xaracterAttribute: IXaracterAttribute = { id: 123 };
        expectedResult = service.addXaracterAttributeToCollectionIfMissing([], xaracterAttribute);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(xaracterAttribute);
      });

      it('should not add a XaracterAttribute to an array that contains it', () => {
        const xaracterAttribute: IXaracterAttribute = { id: 123 };
        const xaracterAttributeCollection: IXaracterAttribute[] = [
          {
            ...xaracterAttribute,
          },
          { id: 456 },
        ];
        expectedResult = service.addXaracterAttributeToCollectionIfMissing(xaracterAttributeCollection, xaracterAttribute);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a XaracterAttribute to an array that doesn't contain it", () => {
        const xaracterAttribute: IXaracterAttribute = { id: 123 };
        const xaracterAttributeCollection: IXaracterAttribute[] = [{ id: 456 }];
        expectedResult = service.addXaracterAttributeToCollectionIfMissing(xaracterAttributeCollection, xaracterAttribute);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(xaracterAttribute);
      });

      it('should add only unique XaracterAttribute to an array', () => {
        const xaracterAttributeArray: IXaracterAttribute[] = [{ id: 123 }, { id: 456 }, { id: 80542 }];
        const xaracterAttributeCollection: IXaracterAttribute[] = [{ id: 123 }];
        expectedResult = service.addXaracterAttributeToCollectionIfMissing(xaracterAttributeCollection, ...xaracterAttributeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const xaracterAttribute: IXaracterAttribute = { id: 123 };
        const xaracterAttribute2: IXaracterAttribute = { id: 456 };
        expectedResult = service.addXaracterAttributeToCollectionIfMissing([], xaracterAttribute, xaracterAttribute2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(xaracterAttribute);
        expect(expectedResult).toContain(xaracterAttribute2);
      });

      it('should accept null and undefined values', () => {
        const xaracterAttribute: IXaracterAttribute = { id: 123 };
        expectedResult = service.addXaracterAttributeToCollectionIfMissing([], null, xaracterAttribute, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(xaracterAttribute);
      });

      it('should return initial array if no XaracterAttribute is added', () => {
        const xaracterAttributeCollection: IXaracterAttribute[] = [{ id: 123 }];
        expectedResult = service.addXaracterAttributeToCollectionIfMissing(xaracterAttributeCollection, undefined, null);
        expect(expectedResult).toEqual(xaracterAttributeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
