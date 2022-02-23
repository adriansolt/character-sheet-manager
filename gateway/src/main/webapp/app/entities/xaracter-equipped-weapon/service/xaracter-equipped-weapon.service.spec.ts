import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Handedness } from 'app/entities/enumerations/handedness.model';
import { IXaracterEquippedWeapon, XaracterEquippedWeapon } from '../xaracter-equipped-weapon.model';

import { XaracterEquippedWeaponService } from './xaracter-equipped-weapon.service';

describe('XaracterEquippedWeapon Service', () => {
  let service: XaracterEquippedWeaponService;
  let httpMock: HttpTestingController;
  let elemDefault: IXaracterEquippedWeapon;
  let expectedResult: IXaracterEquippedWeapon | IXaracterEquippedWeapon[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(XaracterEquippedWeaponService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      xaracterId: 0,
      hand: Handedness.RIGHT,
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

    it('should create a XaracterEquippedWeapon', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new XaracterEquippedWeapon()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a XaracterEquippedWeapon', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          xaracterId: 1,
          hand: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a XaracterEquippedWeapon', () => {
      const patchObject = Object.assign(
        {
          hand: 'BBBBBB',
        },
        new XaracterEquippedWeapon()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of XaracterEquippedWeapon', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          xaracterId: 1,
          hand: 'BBBBBB',
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

    it('should delete a XaracterEquippedWeapon', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addXaracterEquippedWeaponToCollectionIfMissing', () => {
      it('should add a XaracterEquippedWeapon to an empty array', () => {
        const xaracterEquippedWeapon: IXaracterEquippedWeapon = { id: 123 };
        expectedResult = service.addXaracterEquippedWeaponToCollectionIfMissing([], xaracterEquippedWeapon);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(xaracterEquippedWeapon);
      });

      it('should not add a XaracterEquippedWeapon to an array that contains it', () => {
        const xaracterEquippedWeapon: IXaracterEquippedWeapon = { id: 123 };
        const xaracterEquippedWeaponCollection: IXaracterEquippedWeapon[] = [
          {
            ...xaracterEquippedWeapon,
          },
          { id: 456 },
        ];
        expectedResult = service.addXaracterEquippedWeaponToCollectionIfMissing(xaracterEquippedWeaponCollection, xaracterEquippedWeapon);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a XaracterEquippedWeapon to an array that doesn't contain it", () => {
        const xaracterEquippedWeapon: IXaracterEquippedWeapon = { id: 123 };
        const xaracterEquippedWeaponCollection: IXaracterEquippedWeapon[] = [{ id: 456 }];
        expectedResult = service.addXaracterEquippedWeaponToCollectionIfMissing(xaracterEquippedWeaponCollection, xaracterEquippedWeapon);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(xaracterEquippedWeapon);
      });

      it('should add only unique XaracterEquippedWeapon to an array', () => {
        const xaracterEquippedWeaponArray: IXaracterEquippedWeapon[] = [{ id: 123 }, { id: 456 }, { id: 55252 }];
        const xaracterEquippedWeaponCollection: IXaracterEquippedWeapon[] = [{ id: 123 }];
        expectedResult = service.addXaracterEquippedWeaponToCollectionIfMissing(
          xaracterEquippedWeaponCollection,
          ...xaracterEquippedWeaponArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const xaracterEquippedWeapon: IXaracterEquippedWeapon = { id: 123 };
        const xaracterEquippedWeapon2: IXaracterEquippedWeapon = { id: 456 };
        expectedResult = service.addXaracterEquippedWeaponToCollectionIfMissing([], xaracterEquippedWeapon, xaracterEquippedWeapon2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(xaracterEquippedWeapon);
        expect(expectedResult).toContain(xaracterEquippedWeapon2);
      });

      it('should accept null and undefined values', () => {
        const xaracterEquippedWeapon: IXaracterEquippedWeapon = { id: 123 };
        expectedResult = service.addXaracterEquippedWeaponToCollectionIfMissing([], null, xaracterEquippedWeapon, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(xaracterEquippedWeapon);
      });

      it('should return initial array if no XaracterEquippedWeapon is added', () => {
        const xaracterEquippedWeaponCollection: IXaracterEquippedWeapon[] = [{ id: 123 }];
        expectedResult = service.addXaracterEquippedWeaponToCollectionIfMissing(xaracterEquippedWeaponCollection, undefined, null);
        expect(expectedResult).toEqual(xaracterEquippedWeaponCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
