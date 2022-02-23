import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WeaponManeuverDetailComponent } from './weapon-maneuver-detail.component';

describe('WeaponManeuver Management Detail Component', () => {
  let comp: WeaponManeuverDetailComponent;
  let fixture: ComponentFixture<WeaponManeuverDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WeaponManeuverDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ weaponManeuver: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(WeaponManeuverDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(WeaponManeuverDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load weaponManeuver on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.weaponManeuver).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
