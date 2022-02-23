import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CharacterEquippedArmorDetailComponent } from './character-equipped-armor-detail.component';

describe('CharacterEquippedArmor Management Detail Component', () => {
  let comp: CharacterEquippedArmorDetailComponent;
  let fixture: ComponentFixture<CharacterEquippedArmorDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CharacterEquippedArmorDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ characterEquippedArmor: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CharacterEquippedArmorDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CharacterEquippedArmorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load characterEquippedArmor on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.characterEquippedArmor).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
