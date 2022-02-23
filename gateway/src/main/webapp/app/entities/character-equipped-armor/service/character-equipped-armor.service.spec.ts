import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICharacterEquippedArmor, CharacterEquippedArmor } from '../character-equipped-armor.model';

import { CharacterEquippedArmorService } from './character-equipped-armor.service';

describe('CharacterEquippedArmor Service', () => {
  let service: CharacterEquippedArmorService;
  let httpMock: HttpTestingController;
  let elemDefault: ICharacterEquippedArmor;
  let expectedResult: ICharacterEquippedArmor | ICharacterEquippedArmor[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CharacterEquippedArmorService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      characterId: 0,
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

    it('should create a CharacterEquippedArmor', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CharacterEquippedArmor()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CharacterEquippedArmor', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          characterId: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CharacterEquippedArmor', () => {
      const patchObject = Object.assign({}, new CharacterEquippedArmor());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CharacterEquippedArmor', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          characterId: 1,
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

    it('should delete a CharacterEquippedArmor', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCharacterEquippedArmorToCollectionIfMissing', () => {
      it('should add a CharacterEquippedArmor to an empty array', () => {
        const characterEquippedArmor: ICharacterEquippedArmor = { id: 123 };
        expectedResult = service.addCharacterEquippedArmorToCollectionIfMissing([], characterEquippedArmor);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(characterEquippedArmor);
      });

      it('should not add a CharacterEquippedArmor to an array that contains it', () => {
        const characterEquippedArmor: ICharacterEquippedArmor = { id: 123 };
        const characterEquippedArmorCollection: ICharacterEquippedArmor[] = [
          {
            ...characterEquippedArmor,
          },
          { id: 456 },
        ];
        expectedResult = service.addCharacterEquippedArmorToCollectionIfMissing(characterEquippedArmorCollection, characterEquippedArmor);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CharacterEquippedArmor to an array that doesn't contain it", () => {
        const characterEquippedArmor: ICharacterEquippedArmor = { id: 123 };
        const characterEquippedArmorCollection: ICharacterEquippedArmor[] = [{ id: 456 }];
        expectedResult = service.addCharacterEquippedArmorToCollectionIfMissing(characterEquippedArmorCollection, characterEquippedArmor);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(characterEquippedArmor);
      });

      it('should add only unique CharacterEquippedArmor to an array', () => {
        const characterEquippedArmorArray: ICharacterEquippedArmor[] = [{ id: 123 }, { id: 456 }, { id: 3444 }];
        const characterEquippedArmorCollection: ICharacterEquippedArmor[] = [{ id: 123 }];
        expectedResult = service.addCharacterEquippedArmorToCollectionIfMissing(
          characterEquippedArmorCollection,
          ...characterEquippedArmorArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const characterEquippedArmor: ICharacterEquippedArmor = { id: 123 };
        const characterEquippedArmor2: ICharacterEquippedArmor = { id: 456 };
        expectedResult = service.addCharacterEquippedArmorToCollectionIfMissing([], characterEquippedArmor, characterEquippedArmor2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(characterEquippedArmor);
        expect(expectedResult).toContain(characterEquippedArmor2);
      });

      it('should accept null and undefined values', () => {
        const characterEquippedArmor: ICharacterEquippedArmor = { id: 123 };
        expectedResult = service.addCharacterEquippedArmorToCollectionIfMissing([], null, characterEquippedArmor, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(characterEquippedArmor);
      });

      it('should return initial array if no CharacterEquippedArmor is added', () => {
        const characterEquippedArmorCollection: ICharacterEquippedArmor[] = [{ id: 123 }];
        expectedResult = service.addCharacterEquippedArmorToCollectionIfMissing(characterEquippedArmorCollection, undefined, null);
        expect(expectedResult).toEqual(characterEquippedArmorCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
