import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CharacterEquippedWeaponDetailComponent } from './character-equipped-weapon-detail.component';

describe('CharacterEquippedWeapon Management Detail Component', () => {
  let comp: CharacterEquippedWeaponDetailComponent;
  let fixture: ComponentFixture<CharacterEquippedWeaponDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CharacterEquippedWeaponDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ characterEquippedWeapon: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CharacterEquippedWeaponDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CharacterEquippedWeaponDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load characterEquippedWeapon on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.characterEquippedWeapon).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
