import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ManeuverDetailComponent } from './maneuver-detail.component';

describe('Maneuver Management Detail Component', () => {
  let comp: ManeuverDetailComponent;
  let fixture: ComponentFixture<ManeuverDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ManeuverDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ maneuver: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ManeuverDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ManeuverDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load maneuver on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.maneuver).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
