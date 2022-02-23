import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { XaracterEquippedArmorService } from '../service/xaracter-equipped-armor.service';
import { IXaracterEquippedArmor, XaracterEquippedArmor } from '../xaracter-equipped-armor.model';
import { IArmorPiece } from 'app/entities/armor-piece/armor-piece.model';
import { ArmorPieceService } from 'app/entities/armor-piece/service/armor-piece.service';

import { XaracterEquippedArmorUpdateComponent } from './xaracter-equipped-armor-update.component';

describe('XaracterEquippedArmor Management Update Component', () => {
  let comp: XaracterEquippedArmorUpdateComponent;
  let fixture: ComponentFixture<XaracterEquippedArmorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let xaracterEquippedArmorService: XaracterEquippedArmorService;
  let armorPieceService: ArmorPieceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [XaracterEquippedArmorUpdateComponent],
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
      .overrideTemplate(XaracterEquippedArmorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(XaracterEquippedArmorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    xaracterEquippedArmorService = TestBed.inject(XaracterEquippedArmorService);
    armorPieceService = TestBed.inject(ArmorPieceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ArmorPiece query and add missing value', () => {
      const xaracterEquippedArmor: IXaracterEquippedArmor = { id: 456 };
      const armorPiece: IArmorPiece = { id: 83709 };
      xaracterEquippedArmor.armorPiece = armorPiece;

      const armorPieceCollection: IArmorPiece[] = [{ id: 52725 }];
      jest.spyOn(armorPieceService, 'query').mockReturnValue(of(new HttpResponse({ body: armorPieceCollection })));
      const additionalArmorPieces = [armorPiece];
      const expectedCollection: IArmorPiece[] = [...additionalArmorPieces, ...armorPieceCollection];
      jest.spyOn(armorPieceService, 'addArmorPieceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ xaracterEquippedArmor });
      comp.ngOnInit();

      expect(armorPieceService.query).toHaveBeenCalled();
      expect(armorPieceService.addArmorPieceToCollectionIfMissing).toHaveBeenCalledWith(armorPieceCollection, ...additionalArmorPieces);
      expect(comp.armorPiecesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const xaracterEquippedArmor: IXaracterEquippedArmor = { id: 456 };
      const armorPiece: IArmorPiece = { id: 74845 };
      xaracterEquippedArmor.armorPiece = armorPiece;

      activatedRoute.data = of({ xaracterEquippedArmor });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(xaracterEquippedArmor));
      expect(comp.armorPiecesSharedCollection).toContain(armorPiece);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<XaracterEquippedArmor>>();
      const xaracterEquippedArmor = { id: 123 };
      jest.spyOn(xaracterEquippedArmorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ xaracterEquippedArmor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: xaracterEquippedArmor }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(xaracterEquippedArmorService.update).toHaveBeenCalledWith(xaracterEquippedArmor);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<XaracterEquippedArmor>>();
      const xaracterEquippedArmor = new XaracterEquippedArmor();
      jest.spyOn(xaracterEquippedArmorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ xaracterEquippedArmor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: xaracterEquippedArmor }));
      saveSubject.complete();

      // THEN
      expect(xaracterEquippedArmorService.create).toHaveBeenCalledWith(xaracterEquippedArmor);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<XaracterEquippedArmor>>();
      const xaracterEquippedArmor = { id: 123 };
      jest.spyOn(xaracterEquippedArmorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ xaracterEquippedArmor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(xaracterEquippedArmorService.update).toHaveBeenCalledWith(xaracterEquippedArmor);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackArmorPieceById', () => {
      it('Should return tracked ArmorPiece primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackArmorPieceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
