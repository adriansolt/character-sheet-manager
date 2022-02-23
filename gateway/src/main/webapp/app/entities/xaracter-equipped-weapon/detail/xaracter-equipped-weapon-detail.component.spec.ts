import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { XaracterEquippedWeaponDetailComponent } from './xaracter-equipped-weapon-detail.component';

describe('XaracterEquippedWeapon Management Detail Component', () => {
  let comp: XaracterEquippedWeaponDetailComponent;
  let fixture: ComponentFixture<XaracterEquippedWeaponDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [XaracterEquippedWeaponDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ xaracterEquippedWeapon: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(XaracterEquippedWeaponDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(XaracterEquippedWeaponDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load xaracterEquippedWeapon on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.xaracterEquippedWeapon).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
