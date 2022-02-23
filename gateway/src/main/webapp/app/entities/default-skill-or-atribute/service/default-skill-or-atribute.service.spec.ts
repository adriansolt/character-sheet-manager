import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDefaultSkillOrAtribute, DefaultSkillOrAtribute } from '../default-skill-or-atribute.model';

import { DefaultSkillOrAtributeService } from './default-skill-or-atribute.service';

describe('DefaultSkillOrAtribute Service', () => {
  let service: DefaultSkillOrAtributeService;
  let httpMock: HttpTestingController;
  let elemDefault: IDefaultSkillOrAtribute;
  let expectedResult: IDefaultSkillOrAtribute | IDefaultSkillOrAtribute[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DefaultSkillOrAtributeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      modifier: 0,
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

    it('should create a DefaultSkillOrAtribute', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new DefaultSkillOrAtribute()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DefaultSkillOrAtribute', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          modifier: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DefaultSkillOrAtribute', () => {
      const patchObject = Object.assign(
        {
          modifier: 1,
        },
        new DefaultSkillOrAtribute()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DefaultSkillOrAtribute', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          modifier: 1,
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

    it('should delete a DefaultSkillOrAtribute', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDefaultSkillOrAtributeToCollectionIfMissing', () => {
      it('should add a DefaultSkillOrAtribute to an empty array', () => {
        const defaultSkillOrAtribute: IDefaultSkillOrAtribute = { id: 123 };
        expectedResult = service.addDefaultSkillOrAtributeToCollectionIfMissing([], defaultSkillOrAtribute);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(defaultSkillOrAtribute);
      });

      it('should not add a DefaultSkillOrAtribute to an array that contains it', () => {
        const defaultSkillOrAtribute: IDefaultSkillOrAtribute = { id: 123 };
        const defaultSkillOrAtributeCollection: IDefaultSkillOrAtribute[] = [
          {
            ...defaultSkillOrAtribute,
          },
          { id: 456 },
        ];
        expectedResult = service.addDefaultSkillOrAtributeToCollectionIfMissing(defaultSkillOrAtributeCollection, defaultSkillOrAtribute);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DefaultSkillOrAtribute to an array that doesn't contain it", () => {
        const defaultSkillOrAtribute: IDefaultSkillOrAtribute = { id: 123 };
        const defaultSkillOrAtributeCollection: IDefaultSkillOrAtribute[] = [{ id: 456 }];
        expectedResult = service.addDefaultSkillOrAtributeToCollectionIfMissing(defaultSkillOrAtributeCollection, defaultSkillOrAtribute);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(defaultSkillOrAtribute);
      });

      it('should add only unique DefaultSkillOrAtribute to an array', () => {
        const defaultSkillOrAtributeArray: IDefaultSkillOrAtribute[] = [{ id: 123 }, { id: 456 }, { id: 53307 }];
        const defaultSkillOrAtributeCollection: IDefaultSkillOrAtribute[] = [{ id: 123 }];
        expectedResult = service.addDefaultSkillOrAtributeToCollectionIfMissing(
          defaultSkillOrAtributeCollection,
          ...defaultSkillOrAtributeArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const defaultSkillOrAtribute: IDefaultSkillOrAtribute = { id: 123 };
        const defaultSkillOrAtribute2: IDefaultSkillOrAtribute = { id: 456 };
        expectedResult = service.addDefaultSkillOrAtributeToCollectionIfMissing([], defaultSkillOrAtribute, defaultSkillOrAtribute2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(defaultSkillOrAtribute);
        expect(expectedResult).toContain(defaultSkillOrAtribute2);
      });

      it('should accept null and undefined values', () => {
        const defaultSkillOrAtribute: IDefaultSkillOrAtribute = { id: 123 };
        expectedResult = service.addDefaultSkillOrAtributeToCollectionIfMissing([], null, defaultSkillOrAtribute, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(defaultSkillOrAtribute);
      });

      it('should return initial array if no DefaultSkillOrAtribute is added', () => {
        const defaultSkillOrAtributeCollection: IDefaultSkillOrAtribute[] = [{ id: 123 }];
        expectedResult = service.addDefaultSkillOrAtributeToCollectionIfMissing(defaultSkillOrAtributeCollection, undefined, null);
        expect(expectedResult).toEqual(defaultSkillOrAtributeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
