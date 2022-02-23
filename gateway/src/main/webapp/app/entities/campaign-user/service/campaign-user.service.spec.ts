import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICampaignUser, CampaignUser } from '../campaign-user.model';

import { CampaignUserService } from './campaign-user.service';

describe('CampaignUser Service', () => {
  let service: CampaignUserService;
  let httpMock: HttpTestingController;
  let elemDefault: ICampaignUser;
  let expectedResult: ICampaignUser | ICampaignUser[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CampaignUserService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
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

    it('should create a CampaignUser', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CampaignUser()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CampaignUser', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CampaignUser', () => {
      const patchObject = Object.assign({}, new CampaignUser());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CampaignUser', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
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

    it('should delete a CampaignUser', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCampaignUserToCollectionIfMissing', () => {
      it('should add a CampaignUser to an empty array', () => {
        const campaignUser: ICampaignUser = { id: 123 };
        expectedResult = service.addCampaignUserToCollectionIfMissing([], campaignUser);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(campaignUser);
      });

      it('should not add a CampaignUser to an array that contains it', () => {
        const campaignUser: ICampaignUser = { id: 123 };
        const campaignUserCollection: ICampaignUser[] = [
          {
            ...campaignUser,
          },
          { id: 456 },
        ];
        expectedResult = service.addCampaignUserToCollectionIfMissing(campaignUserCollection, campaignUser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CampaignUser to an array that doesn't contain it", () => {
        const campaignUser: ICampaignUser = { id: 123 };
        const campaignUserCollection: ICampaignUser[] = [{ id: 456 }];
        expectedResult = service.addCampaignUserToCollectionIfMissing(campaignUserCollection, campaignUser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(campaignUser);
      });

      it('should add only unique CampaignUser to an array', () => {
        const campaignUserArray: ICampaignUser[] = [{ id: 123 }, { id: 456 }, { id: 79978 }];
        const campaignUserCollection: ICampaignUser[] = [{ id: 123 }];
        expectedResult = service.addCampaignUserToCollectionIfMissing(campaignUserCollection, ...campaignUserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const campaignUser: ICampaignUser = { id: 123 };
        const campaignUser2: ICampaignUser = { id: 456 };
        expectedResult = service.addCampaignUserToCollectionIfMissing([], campaignUser, campaignUser2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(campaignUser);
        expect(expectedResult).toContain(campaignUser2);
      });

      it('should accept null and undefined values', () => {
        const campaignUser: ICampaignUser = { id: 123 };
        expectedResult = service.addCampaignUserToCollectionIfMissing([], null, campaignUser, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(campaignUser);
      });

      it('should return initial array if no CampaignUser is added', () => {
        const campaignUserCollection: ICampaignUser[] = [{ id: 123 }];
        expectedResult = service.addCampaignUserToCollectionIfMissing(campaignUserCollection, undefined, null);
        expect(expectedResult).toEqual(campaignUserCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
