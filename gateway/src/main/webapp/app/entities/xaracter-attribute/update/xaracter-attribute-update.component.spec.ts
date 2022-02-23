import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { XaracterAttributeService } from '../service/xaracter-attribute.service';
import { IXaracterAttribute, XaracterAttribute } from '../xaracter-attribute.model';
import { IXaracter } from 'app/entities/xaracter/xaracter.model';
import { XaracterService } from 'app/entities/xaracter/service/xaracter.service';

import { XaracterAttributeUpdateComponent } from './xaracter-attribute-update.component';

describe('XaracterAttribute Management Update Component', () => {
  let comp: XaracterAttributeUpdateComponent;
  let fixture: ComponentFixture<XaracterAttributeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let xaracterAttributeService: XaracterAttributeService;
  let xaracterService: XaracterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [XaracterAttributeUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(XaracterAttributeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(XaracterAttributeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    xaracterAttributeService = TestBed.inject(XaracterAttributeService);
    xaracterService = TestBed.inject(XaracterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Xaracter query and add missing value', () => {
      const xaracterAttribute: IXaracterAttribute = { id: 456 };
      const xaracterId: IXaracter = { id: 37490 };
      xaracterAttribute.xaracterId = xaracterId;

      const xaracterCollection: IXaracter[] = [{ id: 89264 }];
      jest.spyOn(xaracterService, 'query').mockReturnValue(of(new HttpResponse({ body: xaracterCollection })));
      const additionalXaracters = [xaracterId];
      const expectedCollection: IXaracter[] = [...additionalXaracters, ...xaracterCollection];
      jest.spyOn(xaracterService, 'addXaracterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ xaracterAttribute });
      comp.ngOnInit();

      expect(xaracterService.query).toHaveBeenCalled();
      expect(xaracterService.addXaracterToCollectionIfMissing).toHaveBeenCalledWith(xaracterCollection, ...additionalXaracters);
      expect(comp.xaractersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const xaracterAttribute: IXaracterAttribute = { id: 456 };
      const xaracterId: IXaracter = { id: 13978 };
      xaracterAttribute.xaracterId = xaracterId;

      activatedRoute.data = of({ xaracterAttribute });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(xaracterAttribute));
      expect(comp.xaractersSharedCollection).toContain(xaracterId);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<XaracterAttribute>>();
      const xaracterAttribute = { id: 123 };
      jest.spyOn(xaracterAttributeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ xaracterAttribute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: xaracterAttribute }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(xaracterAttributeService.update).toHaveBeenCalledWith(xaracterAttribute);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<XaracterAttribute>>();
      const xaracterAttribute = new XaracterAttribute();
      jest.spyOn(xaracterAttributeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ xaracterAttribute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: xaracterAttribute }));
      saveSubject.complete();

      // THEN
      expect(xaracterAttributeService.create).toHaveBeenCalledWith(xaracterAttribute);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<XaracterAttribute>>();
      const xaracterAttribute = { id: 123 };
      jest.spyOn(xaracterAttributeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ xaracterAttribute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(xaracterAttributeService.update).toHaveBeenCalledWith(xaracterAttribute);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackXaracterById', () => {
      it('Should return tracked Xaracter primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackXaracterById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
