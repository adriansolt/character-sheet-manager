import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICampaignUser, CampaignUser } from '../campaign-user.model';
import { CampaignUserService } from '../service/campaign-user.service';

import { CampaignUserRoutingResolveService } from './campaign-user-routing-resolve.service';

describe('CampaignUser routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CampaignUserRoutingResolveService;
  let service: CampaignUserService;
  let resultCampaignUser: ICampaignUser | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(CampaignUserRoutingResolveService);
    service = TestBed.inject(CampaignUserService);
    resultCampaignUser = undefined;
  });

  describe('resolve', () => {
    it('should return ICampaignUser returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCampaignUser = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCampaignUser).toEqual({ id: 123 });
    });

    it('should return new ICampaignUser if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCampaignUser = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCampaignUser).toEqual(new CampaignUser());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CampaignUser })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCampaignUser = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCampaignUser).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
