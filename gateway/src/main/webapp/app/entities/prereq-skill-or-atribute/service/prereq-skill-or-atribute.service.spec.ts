import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPrereqSkillOrAtribute, PrereqSkillOrAtribute } from '../prereq-skill-or-atribute.model';

import { PrereqSkillOrAtributeService } from './prereq-skill-or-atribute.service';

describe('PrereqSkillOrAtribute Service', () => {
  let service: PrereqSkillOrAtributeService;
  let httpMock: HttpTestingController;
  let elemDefault: IPrereqSkillOrAtribute;
  let expectedResult: IPrereqSkillOrAtribute | IPrereqSkillOrAtribute[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PrereqSkillOrAtributeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      level: 0,
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

    it('should create a PrereqSkillOrAtribute', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PrereqSkillOrAtribute()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PrereqSkillOrAtribute', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          level: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PrereqSkillOrAtribute', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          level: 1,
        },
        new PrereqSkillOrAtribute()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PrereqSkillOrAtribute', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          level: 1,
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

    it('should delete a PrereqSkillOrAtribute', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPrereqSkillOrAtributeToCollectionIfMissing', () => {
      it('should add a PrereqSkillOrAtribute to an empty array', () => {
        const prereqSkillOrAtribute: IPrereqSkillOrAtribute = { id: 123 };
        expectedResult = service.addPrereqSkillOrAtributeToCollectionIfMissing([], prereqSkillOrAtribute);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prereqSkillOrAtribute);
      });

      it('should not add a PrereqSkillOrAtribute to an array that contains it', () => {
        const prereqSkillOrAtribute: IPrereqSkillOrAtribute = { id: 123 };
        const prereqSkillOrAtributeCollection: IPrereqSkillOrAtribute[] = [
          {
            ...prereqSkillOrAtribute,
          },
          { id: 456 },
        ];
        expectedResult = service.addPrereqSkillOrAtributeToCollectionIfMissing(prereqSkillOrAtributeCollection, prereqSkillOrAtribute);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PrereqSkillOrAtribute to an array that doesn't contain it", () => {
        const prereqSkillOrAtribute: IPrereqSkillOrAtribute = { id: 123 };
        const prereqSkillOrAtributeCollection: IPrereqSkillOrAtribute[] = [{ id: 456 }];
        expectedResult = service.addPrereqSkillOrAtributeToCollectionIfMissing(prereqSkillOrAtributeCollection, prereqSkillOrAtribute);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prereqSkillOrAtribute);
      });

      it('should add only unique PrereqSkillOrAtribute to an array', () => {
        const prereqSkillOrAtributeArray: IPrereqSkillOrAtribute[] = [{ id: 123 }, { id: 456 }, { id: 62583 }];
        const prereqSkillOrAtributeCollection: IPrereqSkillOrAtribute[] = [{ id: 123 }];
        expectedResult = service.addPrereqSkillOrAtributeToCollectionIfMissing(
          prereqSkillOrAtributeCollection,
          ...prereqSkillOrAtributeArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const prereqSkillOrAtribute: IPrereqSkillOrAtribute = { id: 123 };
        const prereqSkillOrAtribute2: IPrereqSkillOrAtribute = { id: 456 };
        expectedResult = service.addPrereqSkillOrAtributeToCollectionIfMissing([], prereqSkillOrAtribute, prereqSkillOrAtribute2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prereqSkillOrAtribute);
        expect(expectedResult).toContain(prereqSkillOrAtribute2);
      });

      it('should accept null and undefined values', () => {
        const prereqSkillOrAtribute: IPrereqSkillOrAtribute = { id: 123 };
        expectedResult = service.addPrereqSkillOrAtributeToCollectionIfMissing([], null, prereqSkillOrAtribute, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prereqSkillOrAtribute);
      });

      it('should return initial array if no PrereqSkillOrAtribute is added', () => {
        const prereqSkillOrAtributeCollection: IPrereqSkillOrAtribute[] = [{ id: 123 }];
        expectedResult = service.addPrereqSkillOrAtributeToCollectionIfMissing(prereqSkillOrAtributeCollection, undefined, null);
        expect(expectedResult).toEqual(prereqSkillOrAtributeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
