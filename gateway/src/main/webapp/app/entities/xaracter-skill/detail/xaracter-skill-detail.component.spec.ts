import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { XaracterSkillDetailComponent } from './xaracter-skill-detail.component';

describe('XaracterSkill Management Detail Component', () => {
  let comp: XaracterSkillDetailComponent;
  let fixture: ComponentFixture<XaracterSkillDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [XaracterSkillDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ xaracterSkill: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(XaracterSkillDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(XaracterSkillDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load xaracterSkill on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.xaracterSkill).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
