import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { AttributeName } from 'app/entities/enumerations/attribute-name.model';
import { ICharacterAttribute, CharacterAttribute } from '../character-attribute.model';

import { CharacterAttributeService } from './character-attribute.service';

describe('CharacterAttribute Service', () => {
  let service: CharacterAttributeService;
  let httpMock: HttpTestingController;
  let elemDefault: ICharacterAttribute;
  let expectedResult: ICharacterAttribute | ICharacterAttribute[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CharacterAttributeService);
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

    it('should create a CharacterAttribute', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CharacterAttribute()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CharacterAttribute', () => {
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

    it('should partial update a CharacterAttribute', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          points: 1,
          attributeModifier: 1,
        },
        new CharacterAttribute()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CharacterAttribute', () => {
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

    it('should delete a CharacterAttribute', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCharacterAttributeToCollectionIfMissing', () => {
      it('should add a CharacterAttribute to an empty array', () => {
        const characterAttribute: ICharacterAttribute = { id: 123 };
        expectedResult = service.addCharacterAttributeToCollectionIfMissing([], characterAttribute);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(characterAttribute);
      });

      it('should not add a CharacterAttribute to an array that contains it', () => {
        const characterAttribute: ICharacterAttribute = { id: 123 };
        const characterAttributeCollection: ICharacterAttribute[] = [
          {
            ...characterAttribute,
          },
          { id: 456 },
        ];
        expectedResult = service.addCharacterAttributeToCollectionIfMissing(characterAttributeCollection, characterAttribute);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CharacterAttribute to an array that doesn't contain it", () => {
        const characterAttribute: ICharacterAttribute = { id: 123 };
        const characterAttributeCollection: ICharacterAttribute[] = [{ id: 456 }];
        expectedResult = service.addCharacterAttributeToCollectionIfMissing(characterAttributeCollection, characterAttribute);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(characterAttribute);
      });

      it('should add only unique CharacterAttribute to an array', () => {
        const characterAttributeArray: ICharacterAttribute[] = [{ id: 123 }, { id: 456 }, { id: 77423 }];
        const characterAttributeCollection: ICharacterAttribute[] = [{ id: 123 }];
        expectedResult = service.addCharacterAttributeToCollectionIfMissing(characterAttributeCollection, ...characterAttributeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const characterAttribute: ICharacterAttribute = { id: 123 };
        const characterAttribute2: ICharacterAttribute = { id: 456 };
        expectedResult = service.addCharacterAttributeToCollectionIfMissing([], characterAttribute, characterAttribute2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(characterAttribute);
        expect(expectedResult).toContain(characterAttribute2);
      });

      it('should accept null and undefined values', () => {
        const characterAttribute: ICharacterAttribute = { id: 123 };
        expectedResult = service.addCharacterAttributeToCollectionIfMissing([], null, characterAttribute, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(characterAttribute);
      });

      it('should return initial array if no CharacterAttribute is added', () => {
        const characterAttributeCollection: ICharacterAttribute[] = [{ id: 123 }];
        expectedResult = service.addCharacterAttributeToCollectionIfMissing(characterAttributeCollection, undefined, null);
        expect(expectedResult).toEqual(characterAttributeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
