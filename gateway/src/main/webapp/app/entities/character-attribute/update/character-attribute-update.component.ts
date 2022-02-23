import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICharacterAttribute, CharacterAttribute } from '../character-attribute.model';
import { CharacterAttributeService } from '../service/character-attribute.service';
import { ICharacter } from 'app/entities/character/character.model';
import { CharacterService } from 'app/entities/character/service/character.service';
import { AttributeName } from 'app/entities/enumerations/attribute-name.model';

@Component({
  selector: 'jhi-character-attribute-update',
  templateUrl: './character-attribute-update.component.html',
})
export class CharacterAttributeUpdateComponent implements OnInit {
  isSaving = false;
  attributeNameValues = Object.keys(AttributeName);

  charactersSharedCollection: ICharacter[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    points: [null, [Validators.required]],
    attributeModifier: [],
    character: [],
  });

  constructor(
    protected characterAttributeService: CharacterAttributeService,
    protected characterService: CharacterService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ characterAttribute }) => {
      this.updateForm(characterAttribute);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const characterAttribute = this.createFromForm();
    if (characterAttribute.id !== undefined) {
      this.subscribeToSaveResponse(this.characterAttributeService.update(characterAttribute));
    } else {
      this.subscribeToSaveResponse(this.characterAttributeService.create(characterAttribute));
    }
  }

  trackCharacterById(index: number, item: ICharacter): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICharacterAttribute>>): void {
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

  protected updateForm(characterAttribute: ICharacterAttribute): void {
    this.editForm.patchValue({
      id: characterAttribute.id,
      name: characterAttribute.name,
      points: characterAttribute.points,
      attributeModifier: characterAttribute.attributeModifier,
      character: characterAttribute.character,
    });

    this.charactersSharedCollection = this.characterService.addCharacterToCollectionIfMissing(
      this.charactersSharedCollection,
      characterAttribute.character
    );
  }

  protected loadRelationshipsOptions(): void {
    this.characterService
      .query()
      .pipe(map((res: HttpResponse<ICharacter[]>) => res.body ?? []))
      .pipe(
        map((characters: ICharacter[]) =>
          this.characterService.addCharacterToCollectionIfMissing(characters, this.editForm.get('character')!.value)
        )
      )
      .subscribe((characters: ICharacter[]) => (this.charactersSharedCollection = characters));
  }

  protected createFromForm(): ICharacterAttribute {
    return {
      ...new CharacterAttribute(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      points: this.editForm.get(['points'])!.value,
      attributeModifier: this.editForm.get(['attributeModifier'])!.value,
      character: this.editForm.get(['character'])!.value,
    };
  }
}
