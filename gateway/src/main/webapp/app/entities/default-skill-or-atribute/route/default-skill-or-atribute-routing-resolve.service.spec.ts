import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IDefaultSkillOrAtribute, DefaultSkillOrAtribute } from '../default-skill-or-atribute.model';
import { DefaultSkillOrAtributeService } from '../service/default-skill-or-atribute.service';

import { DefaultSkillOrAtributeRoutingResolveService } from './default-skill-or-atribute-routing-resolve.service';

describe('DefaultSkillOrAtribute routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DefaultSkillOrAtributeRoutingResolveService;
  let service: DefaultSkillOrAtributeService;
  let resultDefaultSkillOrAtribute: IDefaultSkillOrAtribute | undefined;

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
    routingResolveService = TestBed.inject(DefaultSkillOrAtributeRoutingResolveService);
    service = TestBed.inject(DefaultSkillOrAtributeService);
    resultDefaultSkillOrAtribute = undefined;
  });

  describe('resolve', () => {
    it('should return IDefaultSkillOrAtribute returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDefaultSkillOrAtribute = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDefaultSkillOrAtribute).toEqual({ id: 123 });
    });

    it('should return new IDefaultSkillOrAtribute if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDefaultSkillOrAtribute = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDefaultSkillOrAtribute).toEqual(new DefaultSkillOrAtribute());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DefaultSkillOrAtribute })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDefaultSkillOrAtribute = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDefaultSkillOrAtribute).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
