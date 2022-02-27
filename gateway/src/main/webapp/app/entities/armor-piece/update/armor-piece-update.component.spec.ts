import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ArmorPieceService } from '../service/armor-piece.service';
import { IArmorPiece, ArmorPiece } from '../armor-piece.model';
import { ICharacter } from 'app/entities/character/character.model';
import { CharacterService } from 'app/entities/character/service/character.service';

import { ArmorPieceUpdateComponent } from './armor-piece-update.component';

describe('ArmorPiece Management Update Component', () => {
  let comp: ArmorPieceUpdateComponent;
  let fixture: ComponentFixture<ArmorPieceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let armorPieceService: ArmorPieceService;
  let characterService: CharacterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ArmorPieceUpdateComponent],
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
      .overrideTemplate(ArmorPieceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ArmorPieceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    armorPieceService = TestBed.inject(ArmorPieceService);
    characterService = TestBed.inject(CharacterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Character query and add missing value', () => {
      const armorPiece: IArmorPiece = { id: 456 };
      const character: ICharacter = { id: 16617 };
      armorPiece.character = character;

      const characterCollection: ICharacter[] = [{ id: 55746 }];
      jest.spyOn(characterService, 'query').mockReturnValue(of(new HttpResponse({ body: characterCollection })));
      const additionalCharacters = [character];
      const expectedCollection: ICharacter[] = [...additionalCharacters, ...characterCollection];
      jest.spyOn(characterService, 'addCharacterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ armorPiece });
      comp.ngOnInit();

      expect(characterService.query).toHaveBeenCalled();
      expect(characterService.addCharacterToCollectionIfMissing).toHaveBeenCalledWith(characterCollection, ...additionalCharacters);
      expect(comp.charactersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const armorPiece: IArmorPiece = { id: 456 };
      const character: ICharacter = { id: 26492 };
      armorPiece.character = character;

      activatedRoute.data = of({ armorPiece });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(armorPiece));
      expect(comp.charactersSharedCollection).toContain(character);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ArmorPiece>>();
      const armorPiece = { id: 123 };
      jest.spyOn(armorPieceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ armorPiece });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: armorPiece }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(armorPieceService.update).toHaveBeenCalledWith(armorPiece);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ArmorPiece>>();
      const armorPiece = new ArmorPiece();
      jest.spyOn(armorPieceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ armorPiece });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: armorPiece }));
      saveSubject.complete();

      // THEN
      expect(armorPieceService.create).toHaveBeenCalledWith(armorPiece);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ArmorPiece>>();
      const armorPiece = { id: 123 };
      jest.spyOn(armorPieceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ armorPiece });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(armorPieceService.update).toHaveBeenCalledWith(armorPiece);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCharacterById', () => {
      it('Should return tracked Character primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCharacterById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
