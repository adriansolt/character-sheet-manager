import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IXaracter, Xaracter } from '../xaracter.model';
import { XaracterService } from '../service/xaracter.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { Handedness } from 'app/entities/enumerations/handedness.model';

@Component({
  selector: 'jhi-xaracter-update',
  templateUrl: './xaracter-update.component.html',
})
export class XaracterUpdateComponent implements OnInit {
  isSaving = false;
  handednessValues = Object.keys(Handedness);

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    weight: [null, [Validators.required]],
    height: [null, [Validators.required]],
    points: [null, [Validators.required]],
    picture: [],
    pictureContentType: [],
    handedness: [],
    campaignId: [],
    active: [],
    user: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected xaracterService: XaracterService,
    protected userService: UserService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ xaracter }) => {
      this.updateForm(xaracter);

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
    const xaracter = this.createFromForm();
    if (xaracter.id !== undefined) {
      this.subscribeToSaveResponse(this.xaracterService.update(xaracter));
    } else {
      this.subscribeToSaveResponse(this.xaracterService.create(xaracter));
    }
  }

  trackUserById(index: number, item: IUser): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IXaracter>>): void {
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

  protected updateForm(xaracter: IXaracter): void {
    this.editForm.patchValue({
      id: xaracter.id,
      name: xaracter.name,
      weight: xaracter.weight,
      height: xaracter.height,
      points: xaracter.points,
      picture: xaracter.picture,
      pictureContentType: xaracter.pictureContentType,
      handedness: xaracter.handedness,
      campaignId: xaracter.campaignId,
      active: xaracter.active,
      user: xaracter.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, xaracter.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IXaracter {
    return {
      ...new Xaracter(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      weight: this.editForm.get(['weight'])!.value,
      height: this.editForm.get(['height'])!.value,
      points: this.editForm.get(['points'])!.value,
      pictureContentType: this.editForm.get(['pictureContentType'])!.value,
      picture: this.editForm.get(['picture'])!.value,
      handedness: this.editForm.get(['handedness'])!.value,
      campaignId: this.editForm.get(['campaignId'])!.value,
      active: this.editForm.get(['active'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
