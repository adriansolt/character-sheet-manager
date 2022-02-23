import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { XaracterEquippedArmorDetailComponent } from './xaracter-equipped-armor-detail.component';

describe('XaracterEquippedArmor Management Detail Component', () => {
  let comp: XaracterEquippedArmorDetailComponent;
  let fixture: ComponentFixture<XaracterEquippedArmorDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [XaracterEquippedArmorDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ xaracterEquippedArmor: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(XaracterEquippedArmorDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(XaracterEquippedArmorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load xaracterEquippedArmor on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.xaracterEquippedArmor).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
