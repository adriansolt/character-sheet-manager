import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IXaracterAttribute, XaracterAttribute } from '../xaracter-attribute.model';
import { XaracterAttributeService } from '../service/xaracter-attribute.service';
import { IXaracter } from 'app/entities/xaracter/xaracter.model';
import { XaracterService } from 'app/entities/xaracter/service/xaracter.service';
import { AttributeName } from 'app/entities/enumerations/attribute-name.model';

@Component({
  selector: 'jhi-xaracter-attribute-update',
  templateUrl: './xaracter-attribute-update.component.html',
})
export class XaracterAttributeUpdateComponent implements OnInit {
  isSaving = false;
  attributeNameValues = Object.keys(AttributeName);

  xaractersSharedCollection: IXaracter[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    points: [null, [Validators.required]],
    attributeModifier: [],
    xaracterId: [],
  });

  constructor(
    protected xaracterAttributeService: XaracterAttributeService,
    protected xaracterService: XaracterService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ xaracterAttribute }) => {
      this.updateForm(xaracterAttribute);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const xaracterAttribute = this.createFromForm();
    if (xaracterAttribute.id !== undefined) {
      this.subscribeToSaveResponse(this.xaracterAttributeService.update(xaracterAttribute));
    } else {
      this.subscribeToSaveResponse(this.xaracterAttributeService.create(xaracterAttribute));
    }
  }

  trackXaracterById(index: number, item: IXaracter): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IXaracterAttribute>>): void {
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

  protected updateForm(xaracterAttribute: IXaracterAttribute): void {
    this.editForm.patchValue({
      id: xaracterAttribute.id,
      name: xaracterAttribute.name,
      points: xaracterAttribute.points,
      attributeModifier: xaracterAttribute.attributeModifier,
      xaracterId: xaracterAttribute.xaracterId,
    });

    this.xaractersSharedCollection = this.xaracterService.addXaracterToCollectionIfMissing(
      this.xaractersSharedCollection,
      xaracterAttribute.xaracterId
    );
  }

  protected loadRelationshipsOptions(): void {
    this.xaracterService
      .query()
      .pipe(map((res: HttpResponse<IXaracter[]>) => res.body ?? []))
      .pipe(
        map((xaracters: IXaracter[]) =>
          this.xaracterService.addXaracterToCollectionIfMissing(xaracters, this.editForm.get('xaracterId')!.value)
        )
      )
      .subscribe((xaracters: IXaracter[]) => (this.xaractersSharedCollection = xaracters));
  }

  protected createFromForm(): IXaracterAttribute {
    return {
      ...new XaracterAttribute(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      points: this.editForm.get(['points'])!.value,
      attributeModifier: this.editForm.get(['attributeModifier'])!.value,
      xaracterId: this.editForm.get(['xaracterId'])!.value,
    };
  }
}
