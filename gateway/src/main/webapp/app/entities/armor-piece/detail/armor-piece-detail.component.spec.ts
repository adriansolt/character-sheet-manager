import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ArmorPieceDetailComponent } from './armor-piece-detail.component';

describe('ArmorPiece Management Detail Component', () => {
  let comp: ArmorPieceDetailComponent;
  let fixture: ComponentFixture<ArmorPieceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ArmorPieceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ armorPiece: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ArmorPieceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ArmorPieceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load armorPiece on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.armorPiece).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
