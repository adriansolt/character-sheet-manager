import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CharacterAttributeService } from '../service/character-attribute.service';
import { ICharacterAttribute, CharacterAttribute } from '../character-attribute.model';
import { ICharacter } from 'app/entities/character/character.model';
import { CharacterService } from 'app/entities/character/service/character.service';

import { CharacterAttributeUpdateComponent } from './character-attribute-update.component';

describe('CharacterAttribute Management Update Component', () => {
  let comp: CharacterAttributeUpdateComponent;
  let fixture: ComponentFixture<CharacterAttributeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let characterAttributeService: CharacterAttributeService;
  let characterService: CharacterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CharacterAttributeUpdateComponent],
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
      .overrideTemplate(CharacterAttributeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CharacterAttributeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    characterAttributeService = TestBed.inject(CharacterAttributeService);
    characterService = TestBed.inject(CharacterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Character query and add missing value', () => {
      const characterAttribute: ICharacterAttribute = { id: 456 };
      const character: ICharacter = { id: 10273 };
      characterAttribute.character = character;

      const characterCollection: ICharacter[] = [{ id: 95177 }];
      jest.spyOn(characterService, 'query').mockReturnValue(of(new HttpResponse({ body: characterCollection })));
      const additionalCharacters = [character];
      const expectedCollection: ICharacter[] = [...additionalCharacters, ...characterCollection];
      jest.spyOn(characterService, 'addCharacterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ characterAttribute });
      comp.ngOnInit();

      expect(characterService.query).toHaveBeenCalled();
      expect(characterService.addCharacterToCollectionIfMissing).toHaveBeenCalledWith(characterCollection, ...additionalCharacters);
      expect(comp.charactersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const characterAttribute: ICharacterAttribute = { id: 456 };
      const character: ICharacter = { id: 60197 };
      characterAttribute.character = character;

      activatedRoute.data = of({ characterAttribute });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(characterAttribute));
      expect(comp.charactersSharedCollection).toContain(character);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CharacterAttribute>>();
      const characterAttribute = { id: 123 };
      jest.spyOn(characterAttributeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ characterAttribute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: characterAttribute }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(characterAttributeService.update).toHaveBeenCalledWith(characterAttribute);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CharacterAttribute>>();
      const characterAttribute = new CharacterAttribute();
      jest.spyOn(characterAttributeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ characterAttribute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: characterAttribute }));
      saveSubject.complete();

      // THEN
      expect(characterAttributeService.create).toHaveBeenCalledWith(characterAttribute);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CharacterAttribute>>();
      const characterAttribute = { id: 123 };
      jest.spyOn(characterAttributeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ characterAttribute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(characterAttributeService.update).toHaveBeenCalledWith(characterAttribute);
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
