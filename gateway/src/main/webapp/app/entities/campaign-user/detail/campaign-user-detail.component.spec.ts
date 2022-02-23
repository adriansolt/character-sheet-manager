import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CampaignUserDetailComponent } from './campaign-user-detail.component';

describe('CampaignUser Management Detail Component', () => {
  let comp: CampaignUserDetailComponent;
  let fixture: ComponentFixture<CampaignUserDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CampaignUserDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ campaignUser: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CampaignUserDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CampaignUserDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load campaignUser on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.campaignUser).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
