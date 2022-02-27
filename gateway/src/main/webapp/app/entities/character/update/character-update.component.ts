import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICharacter, Character } from '../character.model';
import { CharacterService } from '../service/character.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ICampaign } from 'app/entities/campaign/campaign.model';
import { CampaignService } from 'app/entities/campaign/service/campaign.service';
import { Handedness } from 'app/entities/enumerations/handedness.model';

@Component({
  selector: 'jhi-character-update',
  templateUrl: './character-update.component.html',
})
export class CharacterUpdateComponent implements OnInit {
  isSaving = false;
  handednessValues = Object.keys(Handedness);

  usersSharedCollection: IUser[] = [];
  campaignsSharedCollection: ICampaign[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    weight: [null, [Validators.required]],
    height: [null, [Validators.required]],
    points: [null, [Validators.required]],
    picture: [],
    pictureContentType: [],
    handedness: [],
    active: [],
    user: [null, Validators.required],
    campaign: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected characterService: CharacterService,
    protected userService: UserService,
    protected campaignService: CampaignService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ character }) => {
      this.updateForm(character);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('gatewayApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const character = this.createFromForm();
    if (character.id !== undefined) {
      this.subscribeToSaveResponse(this.characterService.update(character));
    } else {
      this.subscribeToSaveResponse(this.characterService.create(character));
    }
  }

  trackUserById(index: number, item: IUser): string {
    return item.id!;
  }

  trackCampaignById(index: number, item: ICampaign): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICharacter>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(character: ICharacter): void {
    this.editForm.patchValue({
      id: character.id,
      name: character.name,
      weight: character.weight,
      height: character.height,
      points: character.points,
      picture: character.picture,
      pictureContentType: character.pictureContentType,
      handedness: character.handedness,
      active: character.active,
      user: character.user,
      campaign: character.campaign,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, character.user);
    this.campaignsSharedCollection = this.campaignService.addCampaignToCollectionIfMissing(
      this.campaignsSharedCollection,
      character.campaign
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.campaignService
      .query()
      .pipe(map((res: HttpResponse<ICampaign[]>) => res.body ?? []))
      .pipe(
        map((campaigns: ICampaign[]) =>
          this.campaignService.addCampaignToCollectionIfMissing(campaigns, this.editForm.get('campaign')!.value)
        )
      )
      .subscribe((campaigns: ICampaign[]) => (this.campaignsSharedCollection = campaigns));
  }

  protected createFromForm(): ICharacter {
    return {
      ...new Character(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      weight: this.editForm.get(['weight'])!.value,
      height: this.editForm.get(['height'])!.value,
      points: this.editForm.get(['points'])!.value,
      pictureContentType: this.editForm.get(['pictureContentType'])!.value,
      picture: this.editForm.get(['picture'])!.value,
      handedness: this.editForm.get(['handedness'])!.value,
      active: this.editForm.get(['active'])!.value,
      user: this.editForm.get(['user'])!.value,
      campaign: this.editForm.get(['campaign'])!.value,
    };
  }
}
