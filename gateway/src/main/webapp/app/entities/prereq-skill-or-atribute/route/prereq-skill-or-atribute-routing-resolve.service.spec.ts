import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IPrereqSkillOrAtribute, PrereqSkillOrAtribute } from '../prereq-skill-or-atribute.model';
import { PrereqSkillOrAtributeService } from '../service/prereq-skill-or-atribute.service';

import { PrereqSkillOrAtributeRoutingResolveService } from './prereq-skill-or-atribute-routing-resolve.service';

describe('PrereqSkillOrAtribute routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PrereqSkillOrAtributeRoutingResolveService;
  let service: PrereqSkillOrAtributeService;
  let resultPrereqSkillOrAtribute: IPrereqSkillOrAtribute | undefined;

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
    routingResolveService = TestBed.inject(PrereqSkillOrAtributeRoutingResolveService);
    service = TestBed.inject(PrereqSkillOrAtributeService);
    resultPrereqSkillOrAtribute = undefined;
  });

  describe('resolve', () => {
    it('should return IPrereqSkillOrAtribute returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPrereqSkillOrAtribute = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPrereqSkillOrAtribute).toEqual({ id: 123 });
    });

    it('should return new IPrereqSkillOrAtribute if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPrereqSkillOrAtribute = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPrereqSkillOrAtribute).toEqual(new PrereqSkillOrAtribute());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PrereqSkillOrAtribute })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPrereqSkillOrAtribute = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPrereqSkillOrAtribute).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
