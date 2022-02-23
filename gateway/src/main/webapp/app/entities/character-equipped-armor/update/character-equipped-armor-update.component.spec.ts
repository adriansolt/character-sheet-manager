import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CharacterEquippedArmorService } from '../service/character-equipped-armor.service';
import { ICharacterEquippedArmor, CharacterEquippedArmor } from '../character-equipped-armor.model';
import { IArmorPiece } from 'app/entities/armor-piece/armor-piece.model';
import { ArmorPieceService } from 'app/entities/armor-piece/service/armor-piece.service';

import { CharacterEquippedArmorUpdateComponent } from './character-equipped-armor-update.component';

describe('CharacterEquippedArmor Management Update Component', () => {
  let comp: CharacterEquippedArmorUpdateComponent;
  let fixture: ComponentFixture<CharacterEquippedArmorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let characterEquippedArmorService: CharacterEquippedArmorService;
  let armorPieceService: ArmorPieceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CharacterEquippedArmorUpdateComponent],
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
      .overrideTemplate(CharacterEquippedArmorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CharacterEquippedArmorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    characterEquippedArmorService = TestBed.inject(CharacterEquippedArmorService);
    armorPieceService = TestBed.inject(ArmorPieceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ArmorPiece query and add missing value', () => {
      const characterEquippedArmor: ICharacterEquippedArmor = { id: 456 };
      const armorPiece: IArmorPiece = { id: 21925 };
      characterEquippedArmor.armorPiece = armorPiece;

      const armorPieceCollection: IArmorPiece[] = [{ id: 81553 }];
      jest.spyOn(armorPieceService, 'query').mockReturnValue(of(new HttpResponse({ body: armorPieceCollection })));
      const additionalArmorPieces = [armorPiece];
      const expectedCollection: IArmorPiece[] = [...additionalArmorPieces, ...armorPieceCollection];
      jest.spyOn(armorPieceService, 'addArmorPieceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ characterEquippedArmor });
      comp.ngOnInit();

      expect(armorPieceService.query).toHaveBeenCalled();
      expect(armorPieceService.addArmorPieceToCollectionIfMissing).toHaveBeenCalledWith(armorPieceCollection, ...additionalArmorPieces);
      expect(comp.armorPiecesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const characterEquippedArmor: ICharacterEquippedArmor = { id: 456 };
      const armorPiece: IArmorPiece = { id: 56511 };
      characterEquippedArmor.armorPiece = armorPiece;

      activatedRoute.data = of({ characterEquippedArmor });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(characterEquippedArmor));
      expect(comp.armorPiecesSharedCollection).toContain(armorPiece);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CharacterEquippedArmor>>();
      const characterEquippedArmor = { id: 123 };
      jest.spyOn(characterEquippedArmorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ characterEquippedArmor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: characterEquippedArmor }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(characterEquippedArmorService.update).toHaveBeenCalledWith(characterEquippedArmor);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CharacterEquippedArmor>>();
      const characterEquippedArmor = new CharacterEquippedArmor();
      jest.spyOn(characterEquippedArmorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ characterEquippedArmor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: characterEquippedArmor }));
      saveSubject.complete();

      // THEN
      expect(characterEquippedArmorService.create).toHaveBeenCalledWith(characterEquippedArmor);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CharacterEquippedArmor>>();
      const characterEquippedArmor = { id: 123 };
      jest.spyOn(characterEquippedArmorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ characterEquippedArmor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(characterEquippedArmorService.update).toHaveBeenCalledWith(characterEquippedArmor);
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
