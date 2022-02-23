import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IArmorPiece, ArmorPiece } from '../armor-piece.model';
import { ArmorPieceService } from '../service/armor-piece.service';

import { ArmorPieceRoutingResolveService } from './armor-piece-routing-resolve.service';

describe('ArmorPiece routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ArmorPieceRoutingResolveService;
  let service: ArmorPieceService;
  let resultArmorPiece: IArmorPiece | undefined;

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
    routingResolveService = TestBed.inject(ArmorPieceRoutingResolveService);
    service = TestBed.inject(ArmorPieceService);
    resultArmorPiece = undefined;
  });

  describe('resolve', () => {
    it('should return IArmorPiece returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultArmorPiece = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultArmorPiece).toEqual({ id: 123 });
    });

    it('should return new IArmorPiece if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultArmorPiece = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultArmorPiece).toEqual(new ArmorPiece());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ArmorPiece })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultArmorPiece = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultArmorPiece).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
