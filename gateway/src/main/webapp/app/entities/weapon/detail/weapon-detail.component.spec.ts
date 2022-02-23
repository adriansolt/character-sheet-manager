import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WeaponDetailComponent } from './weapon-detail.component';

describe('Weapon Management Detail Component', () => {
  let comp: WeaponDetailComponent;
  let fixture: ComponentFixture<WeaponDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WeaponDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ weapon: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(WeaponDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(WeaponDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load weapon on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.weapon).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
