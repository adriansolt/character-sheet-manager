import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IWeapon, Weapon } from '../weapon.model';

import { WeaponService } from './weapon.service';

describe('Weapon Service', () => {
  let service: WeaponService;
  let httpMock: HttpTestingController;
  let elemDefault: IWeapon;
  let expectedResult: IWeapon | IWeapon[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WeaponService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      reach: 0,
      baseDamage: 0,
      requiredST: 0,
      damageModifier: 0,
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

    it('should create a Weapon', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Weapon()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Weapon', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reach: 1,
          baseDamage: 1,
          requiredST: 1,
          damageModifier: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Weapon', () => {
      const patchObject = Object.assign(
        {
          reach: 1,
          damageModifier: 1,
        },
        new Weapon()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Weapon', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          reach: 1,
          baseDamage: 1,
          requiredST: 1,
          damageModifier: 1,
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

    it('should delete a Weapon', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addWeaponToCollectionIfMissing', () => {
      it('should add a Weapon to an empty array', () => {
        const weapon: IWeapon = { id: 123 };
        expectedResult = service.addWeaponToCollectionIfMissing([], weapon);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(weapon);
      });

      it('should not add a Weapon to an array that contains it', () => {
        const weapon: IWeapon = { id: 123 };
        const weaponCollection: IWeapon[] = [
          {
            ...weapon,
          },
          { id: 456 },
        ];
        expectedResult = service.addWeaponToCollectionIfMissing(weaponCollection, weapon);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Weapon to an array that doesn't contain it", () => {
        const weapon: IWeapon = { id: 123 };
        const weaponCollection: IWeapon[] = [{ id: 456 }];
        expectedResult = service.addWeaponToCollectionIfMissing(weaponCollection, weapon);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(weapon);
      });

      it('should add only unique Weapon to an array', () => {
        const weaponArray: IWeapon[] = [{ id: 123 }, { id: 456 }, { id: 52399 }];
        const weaponCollection: IWeapon[] = [{ id: 123 }];
        expectedResult = service.addWeaponToCollectionIfMissing(weaponCollection, ...weaponArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const weapon: IWeapon = { id: 123 };
        const weapon2: IWeapon = { id: 456 };
        expectedResult = service.addWeaponToCollectionIfMissing([], weapon, weapon2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(weapon);
        expect(expectedResult).toContain(weapon2);
      });

      it('should accept null and undefined values', () => {
        const weapon: IWeapon = { id: 123 };
        expectedResult = service.addWeaponToCollectionIfMissing([], null, weapon, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(weapon);
      });

      it('should return initial array if no Weapon is added', () => {
        const weaponCollection: IWeapon[] = [{ id: 123 }];
        expectedResult = service.addWeaponToCollectionIfMissing(weaponCollection, undefined, null);
        expect(expectedResult).toEqual(weaponCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
