import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { WeaponService } from '../service/weapon.service';
import { IWeapon, Weapon } from '../weapon.model';
import { ICampaign } from 'app/entities/campaign/campaign.model';
import { CampaignService } from 'app/entities/campaign/service/campaign.service';
import { ICharacter } from 'app/entities/character/character.model';
import { CharacterService } from 'app/entities/character/service/character.service';

import { WeaponUpdateComponent } from './weapon-update.component';

describe('Weapon Management Update Component', () => {
  let comp: WeaponUpdateComponent;
  let fixture: ComponentFixture<WeaponUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let weaponService: WeaponService;
  let campaignService: CampaignService;
  let characterService: CharacterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [WeaponUpdateComponent],
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
      .overrideTemplate(WeaponUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WeaponUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    weaponService = TestBed.inject(WeaponService);
    campaignService = TestBed.inject(CampaignService);
    characterService = TestBed.inject(CharacterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Campaign query and add missing value', () => {
      const weapon: IWeapon = { id: 456 };
      const campaign: ICampaign = { id: 60847 };
      weapon.campaign = campaign;

      const campaignCollection: ICampaign[] = [{ id: 18343 }];
      jest.spyOn(campaignService, 'query').mockReturnValue(of(new HttpResponse({ body: campaignCollection })));
      const additionalCampaigns = [campaign];
      const expectedCollection: ICampaign[] = [...additionalCampaigns, ...campaignCollection];
      jest.spyOn(campaignService, 'addCampaignToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ weapon });
      comp.ngOnInit();

      expect(campaignService.query).toHaveBeenCalled();
      expect(campaignService.addCampaignToCollectionIfMissing).toHaveBeenCalledWith(campaignCollection, ...additionalCampaigns);
      expect(comp.campaignsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Character query and add missing value', () => {
      const weapon: IWeapon = { id: 456 };
      const character: ICharacter = { id: 99646 };
      weapon.character = character;

      const characterCollection: ICharacter[] = [{ id: 38116 }];
      jest.spyOn(characterService, 'query').mockReturnValue(of(new HttpResponse({ body: characterCollection })));
      const additionalCharacters = [character];
      const expectedCollection: ICharacter[] = [...additionalCharacters, ...characterCollection];
      jest.spyOn(characterService, 'addCharacterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ weapon });
      comp.ngOnInit();

      expect(characterService.query).toHaveBeenCalled();
      expect(characterService.addCharacterToCollectionIfMissing).toHaveBeenCalledWith(characterCollection, ...additionalCharacters);
      expect(comp.charactersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const weapon: IWeapon = { id: 456 };
      const campaign: ICampaign = { id: 9961 };
      weapon.campaign = campaign;
      const character: ICharacter = { id: 66994 };
      weapon.character = character;

      activatedRoute.data = of({ weapon });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(weapon));
      expect(comp.campaignsSharedCollection).toContain(campaign);
      expect(comp.charactersSharedCollection).toContain(character);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Weapon>>();
      const weapon = { id: 123 };
      jest.spyOn(weaponService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ weapon });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: weapon }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(weaponService.update).toHaveBeenCalledWith(weapon);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Weapon>>();
      const weapon = new Weapon();
      jest.spyOn(weaponService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ weapon });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: weapon }));
      saveSubject.complete();

      // THEN
      expect(weaponService.create).toHaveBeenCalledWith(weapon);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Weapon>>();
      const weapon = { id: 123 };
      jest.spyOn(weaponService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ weapon });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(weaponService.update).toHaveBeenCalledWith(weapon);
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

    describe('trackCharacterById', () => {
      it('Should return tracked Character primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCharacterById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
