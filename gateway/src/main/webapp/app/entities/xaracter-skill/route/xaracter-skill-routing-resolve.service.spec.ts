import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IXaracterSkill, XaracterSkill } from '../xaracter-skill.model';
import { XaracterSkillService } from '../service/xaracter-skill.service';

import { XaracterSkillRoutingResolveService } from './xaracter-skill-routing-resolve.service';

describe('XaracterSkill routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: XaracterSkillRoutingResolveService;
  let service: XaracterSkillService;
  let resultXaracterSkill: IXaracterSkill | undefined;

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
    routingResolveService = TestBed.inject(XaracterSkillRoutingResolveService);
    service = TestBed.inject(XaracterSkillService);
    resultXaracterSkill = undefined;
  });

  describe('resolve', () => {
    it('should return IXaracterSkill returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultXaracterSkill = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultXaracterSkill).toEqual({ id: 123 });
    });

    it('should return new IXaracterSkill if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultXaracterSkill = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultXaracterSkill).toEqual(new XaracterSkill());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as XaracterSkill })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultXaracterSkill = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultXaracterSkill).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
