import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PrereqSkillOrAtributeDetailComponent } from './prereq-skill-or-atribute-detail.component';

describe('PrereqSkillOrAtribute Management Detail Component', () => {
  let comp: PrereqSkillOrAtributeDetailComponent;
  let fixture: ComponentFixture<PrereqSkillOrAtributeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PrereqSkillOrAtributeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ prereqSkillOrAtribute: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PrereqSkillOrAtributeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PrereqSkillOrAtributeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load prereqSkillOrAtribute on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.prereqSkillOrAtribute).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
