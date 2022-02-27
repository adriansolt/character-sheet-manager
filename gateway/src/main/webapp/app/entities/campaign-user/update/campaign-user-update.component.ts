import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICampaignUser, CampaignUser } from '../campaign-user.model';
import { CampaignUserService } from '../service/campaign-user.service';
import { ICampaign } from 'app/entities/campaign/campaign.model';
import { CampaignService } from 'app/entities/campaign/service/campaign.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-campaign-user-update',
  templateUrl: './campaign-user-update.component.html',
})
export class CampaignUserUpdateComponent implements OnInit {
  isSaving = false;

  campaignsSharedCollection: ICampaign[] = [];
  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    campaign: [null, Validators.required],
    user: [null, Validators.required],
  });

  constructor(
    protected campaignUserService: CampaignUserService,
    protected campaignService: CampaignService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ campaignUser }) => {
      this.updateForm(campaignUser);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const campaignUser = this.createFromForm();
    if (campaignUser.id !== undefined) {
      this.subscribeToSaveResponse(this.campaignUserService.update(campaignUser));
    } else {
      this.subscribeToSaveResponse(this.campaignUserService.create(campaignUser));
    }
  }

  trackCampaignById(index: number, item: ICampaign): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICampaignUser>>): void {
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

  protected updateForm(campaignUser: ICampaignUser): void {
    this.editForm.patchValue({
      id: campaignUser.id,
      campaign: campaignUser.campaign,
      user: campaignUser.user,
    });

    this.campaignsSharedCollection = this.campaignService.addCampaignToCollectionIfMissing(
      this.campaignsSharedCollection,
      campaignUser.campaign
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, campaignUser.user);
  }

  protected loadRelationshipsOptions(): void {
    this.campaignService
      .query()
      .pipe(map((res: HttpResponse<ICampaign[]>) => res.body ?? []))
      .pipe(
        map((campaigns: ICampaign[]) =>
          this.campaignService.addCampaignToCollectionIfMissing(campaigns, this.editForm.get('campaign')!.value)
        )
      )
      .subscribe((campaigns: ICampaign[]) => (this.campaignsSharedCollection = campaigns));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): ICampaignUser {
    return {
      ...new CampaignUser(),
      id: this.editForm.get(['id'])!.value,
      campaign: this.editForm.get(['campaign'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
