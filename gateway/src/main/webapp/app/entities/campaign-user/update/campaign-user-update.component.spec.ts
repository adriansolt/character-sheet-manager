import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CampaignUserService } from '../service/campaign-user.service';
import { ICampaignUser, CampaignUser } from '../campaign-user.model';
import { ICampaign } from 'app/entities/campaign/campaign.model';
import { CampaignService } from 'app/entities/campaign/service/campaign.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { CampaignUserUpdateComponent } from './campaign-user-update.component';

describe('CampaignUser Management Update Component', () => {
  let comp: CampaignUserUpdateComponent;
  let fixture: ComponentFixture<CampaignUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let campaignUserService: CampaignUserService;
  let campaignService: CampaignService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CampaignUserUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CampaignUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CampaignUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    campaignUserService = TestBed.inject(CampaignUserService);
    campaignService = TestBed.inject(CampaignService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Campaign query and add missing value', () => {
      const campaignUser: ICampaignUser = { id: 456 };
      const campaignId: ICampaign = { id: 10062 };
      campaignUser.campaignId = campaignId;

      const campaignCollection: ICampaign[] = [{ id: 22665 }];
      jest.spyOn(campaignService, 'query').mockReturnValue(of(new HttpResponse({ body: campaignCollection })));
      const additionalCampaigns = [campaignId];
      const expectedCollection: ICampaign[] = [...additionalCampaigns, ...campaignCollection];
      jest.spyOn(campaignService, 'addCampaignToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ campaignUser });
      comp.ngOnInit();

      expect(campaignService.query).toHaveBeenCalled();
      expect(campaignService.addCampaignToCollectionIfMissing).toHaveBeenCalledWith(campaignCollection, ...additionalCampaigns);
      expect(comp.campaignsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const campaignUser: ICampaignUser = { id: 456 };
      const user: IUser = { id: '81329dcd-4e89-4db7-904f-3830dc2aae54' };
      campaignUser.user = user;

      const userCollection: IUser[] = [{ id: 'e26fc975-ad49-4291-ad0a-5e5b3ade554f' }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ campaignUser });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const campaignUser: ICampaignUser = { id: 456 };
      const campaignId: ICampaign = { id: 74734 };
      campaignUser.campaignId = campaignId;
      const user: IUser = { id: 'e0a1dcca-7801-4375-8c1b-698783774687' };
      campaignUser.user = user;

      activatedRoute.data = of({ campaignUser });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(campaignUser));
      expect(comp.campaignsSharedCollection).toContain(campaignId);
      expect(comp.usersSharedCollection).toContain(user);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CampaignUser>>();
      const campaignUser = { id: 123 };
      jest.spyOn(campaignUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ campaignUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: campaignUser }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(campaignUserService.update).toHaveBeenCalledWith(campaignUser);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CampaignUser>>();
      const campaignUser = new CampaignUser();
      jest.spyOn(campaignUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ campaignUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: campaignUser }));
      saveSubject.complete();

      // THEN
      expect(campaignUserService.create).toHaveBeenCalledWith(campaignUser);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CampaignUser>>();
      const campaignUser = { id: 123 };
      jest.spyOn(campaignUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ campaignUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(campaignUserService.update).toHaveBeenCalledWith(campaignUser);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCampaignById', () => {
      it('Should return tracked Campaign primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCampaignById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackUserById', () => {
      it('Should return tracked User primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
