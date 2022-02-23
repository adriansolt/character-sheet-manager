import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CharacterSkillService } from '../service/character-skill.service';
import { ICharacterSkill, CharacterSkill } from '../character-skill.model';
import { ICharacter } from 'app/entities/character/character.model';
import { CharacterService } from 'app/entities/character/service/character.service';
import { ISkill } from 'app/entities/skill/skill.model';
import { SkillService } from 'app/entities/skill/service/skill.service';

import { CharacterSkillUpdateComponent } from './character-skill-update.component';

describe('CharacterSkill Management Update Component', () => {
  let comp: CharacterSkillUpdateComponent;
  let fixture: ComponentFixture<CharacterSkillUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let characterSkillService: CharacterSkillService;
  let characterService: CharacterService;
  let skillService: SkillService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CharacterSkillUpdateComponent],
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
      .overrideTemplate(CharacterSkillUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CharacterSkillUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    characterSkillService = TestBed.inject(CharacterSkillService);
    characterService = TestBed.inject(CharacterService);
    skillService = TestBed.inject(SkillService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Character query and add missing value', () => {
      const characterSkill: ICharacterSkill = { id: 456 };
      const character: ICharacter = { id: 42170 };
      characterSkill.character = character;

      const characterCollection: ICharacter[] = [{ id: 87933 }];
      jest.spyOn(characterService, 'query').mockReturnValue(of(new HttpResponse({ body: characterCollection })));
      const additionalCharacters = [character];
      const expectedCollection: ICharacter[] = [...additionalCharacters, ...characterCollection];
      jest.spyOn(characterService, 'addCharacterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ characterSkill });
      comp.ngOnInit();

      expect(characterService.query).toHaveBeenCalled();
      expect(characterService.addCharacterToCollectionIfMissing).toHaveBeenCalledWith(characterCollection, ...additionalCharacters);
      expect(comp.charactersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Skill query and add missing value', () => {
      const characterSkill: ICharacterSkill = { id: 456 };
      const skill: ISkill = { id: 69014 };
      characterSkill.skill = skill;

      const skillCollection: ISkill[] = [{ id: 32400 }];
      jest.spyOn(skillService, 'query').mockReturnValue(of(new HttpResponse({ body: skillCollection })));
      const additionalSkills = [skill];
      const expectedCollection: ISkill[] = [...additionalSkills, ...skillCollection];
      jest.spyOn(skillService, 'addSkillToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ characterSkill });
      comp.ngOnInit();

      expect(skillService.query).toHaveBeenCalled();
      expect(skillService.addSkillToCollectionIfMissing).toHaveBeenCalledWith(skillCollection, ...additionalSkills);
      expect(comp.skillsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const characterSkill: ICharacterSkill = { id: 456 };
      const character: ICharacter = { id: 45608 };
      characterSkill.character = character;
      const skill: ISkill = { id: 522 };
      characterSkill.skill = skill;

      activatedRoute.data = of({ characterSkill });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(characterSkill));
      expect(comp.charactersSharedCollection).toContain(character);
      expect(comp.skillsSharedCollection).toContain(skill);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CharacterSkill>>();
      const characterSkill = { id: 123 };
      jest.spyOn(characterSkillService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ characterSkill });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: characterSkill }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(characterSkillService.update).toHaveBeenCalledWith(characterSkill);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CharacterSkill>>();
      const characterSkill = new CharacterSkill();
      jest.spyOn(characterSkillService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ characterSkill });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: characterSkill }));
      saveSubject.complete();

      // THEN
      expect(characterSkillService.create).toHaveBeenCalledWith(characterSkill);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CharacterSkill>>();
      const characterSkill = { id: 123 };
      jest.spyOn(characterSkillService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ characterSkill });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(characterSkillService.update).toHaveBeenCalledWith(characterSkill);
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

    describe('trackSkillById', () => {
      it('Should return tracked Skill primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSkillById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
