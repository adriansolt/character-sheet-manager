import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PrereqSkillOrAtributeService } from '../service/prereq-skill-or-atribute.service';
import { IPrereqSkillOrAtribute, PrereqSkillOrAtribute } from '../prereq-skill-or-atribute.model';
import { ISkill } from 'app/entities/skill/skill.model';
import { SkillService } from 'app/entities/skill/service/skill.service';

import { PrereqSkillOrAtributeUpdateComponent } from './prereq-skill-or-atribute-update.component';

describe('PrereqSkillOrAtribute Management Update Component', () => {
  let comp: PrereqSkillOrAtributeUpdateComponent;
  let fixture: ComponentFixture<PrereqSkillOrAtributeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let prereqSkillOrAtributeService: PrereqSkillOrAtributeService;
  let skillService: SkillService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PrereqSkillOrAtributeUpdateComponent],
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
      .overrideTemplate(PrereqSkillOrAtributeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PrereqSkillOrAtributeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    prereqSkillOrAtributeService = TestBed.inject(PrereqSkillOrAtributeService);
    skillService = TestBed.inject(SkillService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Skill query and add missing value', () => {
      const prereqSkillOrAtribute: IPrereqSkillOrAtribute = { id: 456 };
      const skillId: ISkill = { id: 1329 };
      prereqSkillOrAtribute.skillId = skillId;

      const skillCollection: ISkill[] = [{ id: 47274 }];
      jest.spyOn(skillService, 'query').mockReturnValue(of(new HttpResponse({ body: skillCollection })));
      const additionalSkills = [skillId];
      const expectedCollection: ISkill[] = [...additionalSkills, ...skillCollection];
      jest.spyOn(skillService, 'addSkillToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prereqSkillOrAtribute });
      comp.ngOnInit();

      expect(skillService.query).toHaveBeenCalled();
      expect(skillService.addSkillToCollectionIfMissing).toHaveBeenCalledWith(skillCollection, ...additionalSkills);
      expect(comp.skillsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const prereqSkillOrAtribute: IPrereqSkillOrAtribute = { id: 456 };
      const skillId: ISkill = { id: 92381 };
      prereqSkillOrAtribute.skillId = skillId;

      activatedRoute.data = of({ prereqSkillOrAtribute });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(prereqSkillOrAtribute));
      expect(comp.skillsSharedCollection).toContain(skillId);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PrereqSkillOrAtribute>>();
      const prereqSkillOrAtribute = { id: 123 };
      jest.spyOn(prereqSkillOrAtributeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prereqSkillOrAtribute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prereqSkillOrAtribute }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(prereqSkillOrAtributeService.update).toHaveBeenCalledWith(prereqSkillOrAtribute);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PrereqSkillOrAtribute>>();
      const prereqSkillOrAtribute = new PrereqSkillOrAtribute();
      jest.spyOn(prereqSkillOrAtributeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prereqSkillOrAtribute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prereqSkillOrAtribute }));
      saveSubject.complete();

      // THEN
      expect(prereqSkillOrAtributeService.create).toHaveBeenCalledWith(prereqSkillOrAtribute);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PrereqSkillOrAtribute>>();
      const prereqSkillOrAtribute = { id: 123 };
      jest.spyOn(prereqSkillOrAtributeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prereqSkillOrAtribute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(prereqSkillOrAtributeService.update).toHaveBeenCalledWith(prereqSkillOrAtribute);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSkillById', () => {
      it('Should return tracked Skill primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSkillById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
