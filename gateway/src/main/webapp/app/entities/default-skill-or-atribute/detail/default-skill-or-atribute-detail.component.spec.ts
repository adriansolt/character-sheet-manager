import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DefaultSkillOrAtributeDetailComponent } from './default-skill-or-atribute-detail.component';

describe('DefaultSkillOrAtribute Management Detail Component', () => {
  let comp: DefaultSkillOrAtributeDetailComponent;
  let fixture: ComponentFixture<DefaultSkillOrAtributeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DefaultSkillOrAtributeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ defaultSkillOrAtribute: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DefaultSkillOrAtributeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DefaultSkillOrAtributeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load defaultSkillOrAtribute on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.defaultSkillOrAtribute).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
