import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CharacterAttributeDetailComponent } from './character-attribute-detail.component';

describe('CharacterAttribute Management Detail Component', () => {
  let comp: CharacterAttributeDetailComponent;
  let fixture: ComponentFixture<CharacterAttributeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CharacterAttributeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ characterAttribute: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CharacterAttributeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CharacterAttributeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load characterAttribute on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.characterAttribute).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
