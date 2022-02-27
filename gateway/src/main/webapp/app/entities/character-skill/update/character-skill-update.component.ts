import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICharacterSkill, CharacterSkill } from '../character-skill.model';
import { CharacterSkillService } from '../service/character-skill.service';
import { ICharacter } from 'app/entities/character/character.model';
import { CharacterService } from 'app/entities/character/service/character.service';
import { ISkill } from 'app/entities/skill/skill.model';
import { SkillService } from 'app/entities/skill/service/skill.service';

@Component({
  selector: 'jhi-character-skill-update',
  templateUrl: './character-skill-update.component.html',
})
export class CharacterSkillUpdateComponent implements OnInit {
  isSaving = false;

  charactersSharedCollection: ICharacter[] = [];
  skillsSharedCollection: ISkill[] = [];

  editForm = this.fb.group({
    id: [],
    points: [null, [Validators.required]],
    skillModifier: [null, [Validators.required]],
    character: [],
    skill: [],
  });

  constructor(
    protected characterSkillService: CharacterSkillService,
    protected characterService: CharacterService,
    protected skillService: SkillService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ characterSkill }) => {
      this.updateForm(characterSkill);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const characterSkill = this.createFromForm();
    if (characterSkill.id !== undefined) {
      this.subscribeToSaveResponse(this.characterSkillService.update(characterSkill));
    } else {
      this.subscribeToSaveResponse(this.characterSkillService.create(characterSkill));
    }
  }

  trackCharacterById(index: number, item: ICharacter): number {
    return item.id!;
  }

  trackSkillById(index: number, item: ISkill): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICharacterSkill>>): void {
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

  protected updateForm(characterSkill: ICharacterSkill): void {
    this.editForm.patchValue({
      id: characterSkill.id,
      points: characterSkill.points,
      skillModifier: characterSkill.skillModifier,
      character: characterSkill.character,
      skill: characterSkill.skill,
    });

    this.charactersSharedCollection = this.characterService.addCharacterToCollectionIfMissing(
      this.charactersSharedCollection,
      characterSkill.character
    );
    this.skillsSharedCollection = this.skillService.addSkillToCollectionIfMissing(this.skillsSharedCollection, characterSkill.skill);
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

    this.skillService
      .query()
      .pipe(map((res: HttpResponse<ISkill[]>) => res.body ?? []))
      .pipe(map((skills: ISkill[]) => this.skillService.addSkillToCollectionIfMissing(skills, this.editForm.get('skill')!.value)))
      .subscribe((skills: ISkill[]) => (this.skillsSharedCollection = skills));
  }

  protected createFromForm(): ICharacterSkill {
    return {
      ...new CharacterSkill(),
      id: this.editForm.get(['id'])!.value,
      points: this.editForm.get(['points'])!.value,
      skillModifier: this.editForm.get(['skillModifier'])!.value,
      character: this.editForm.get(['character'])!.value,
      skill: this.editForm.get(['skill'])!.value,
    };
  }
}
