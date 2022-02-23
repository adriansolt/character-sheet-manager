import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { INote, Note } from '../note.model';
import { NoteService } from '../service/note.service';
import { ICharacter } from 'app/entities/character/character.model';
import { CharacterService } from 'app/entities/character/service/character.service';

@Component({
  selector: 'jhi-note-update',
  templateUrl: './note-update.component.html',
})
export class NoteUpdateComponent implements OnInit {
  isSaving = false;

  charactersSharedCollection: ICharacter[] = [];

  editForm = this.fb.group({
    id: [],
    description: [],
    character: [],
  });

  constructor(
    protected noteService: NoteService,
    protected characterService: CharacterService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ note }) => {
      this.updateForm(note);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const note = this.createFromForm();
    if (note.id !== undefined) {
      this.subscribeToSaveResponse(this.noteService.update(note));
    } else {
      this.subscribeToSaveResponse(this.noteService.create(note));
    }
  }

  trackCharacterById(index: number, item: ICharacter): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INote>>): void {
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

  protected updateForm(note: INote): void {
    this.editForm.patchValue({
      id: note.id,
      description: note.description,
      character: note.character,
    });

    this.charactersSharedCollection = this.characterService.addCharacterToCollectionIfMissing(
      this.charactersSharedCollection,
      note.character
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

  protected createFromForm(): INote {
    return {
      ...new Note(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      character: this.editForm.get(['character'])!.value,
    };
  }
}
