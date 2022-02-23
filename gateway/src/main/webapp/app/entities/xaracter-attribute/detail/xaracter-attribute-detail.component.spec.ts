import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { XaracterAttributeDetailComponent } from './xaracter-attribute-detail.component';

describe('XaracterAttribute Management Detail Component', () => {
  let comp: XaracterAttributeDetailComponent;
  let fixture: ComponentFixture<XaracterAttributeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [XaracterAttributeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ xaracterAttribute: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(XaracterAttributeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(XaracterAttributeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load xaracterAttribute on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.xaracterAttribute).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
