import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CharacterSkillDetailComponent } from './character-skill-detail.component';

describe('CharacterSkill Management Detail Component', () => {
  let comp: CharacterSkillDetailComponent;
  let fixture: ComponentFixture<CharacterSkillDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CharacterSkillDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ characterSkill: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CharacterSkillDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CharacterSkillDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load characterSkill on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.characterSkill).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
