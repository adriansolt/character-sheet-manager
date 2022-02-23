import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { INote, Note } from '../note.model';
import { NoteService } from '../service/note.service';
import { IXaracter } from 'app/entities/xaracter/xaracter.model';
import { XaracterService } from 'app/entities/xaracter/service/xaracter.service';

@Component({
  selector: 'jhi-note-update',
  templateUrl: './note-update.component.html',
})
export class NoteUpdateComponent implements OnInit {
  isSaving = false;

  xaractersSharedCollection: IXaracter[] = [];

  editForm = this.fb.group({
    id: [],
    description: [],
    xaracterId: [],
  });

  constructor(
    protected noteService: NoteService,
    protected xaracterService: XaracterService,
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

  trackXaracterById(index: number, item: IXaracter): number {
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
      xaracterId: note.xaracterId,
    });

    this.xaractersSharedCollection = this.xaracterService.addXaracterToCollectionIfMissing(this.xaractersSharedCollection, note.xaracterId);
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

  protected createFromForm(): INote {
    return {
      ...new Note(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      xaracterId: this.editForm.get(['xaracterId'])!.value,
    };
  }
}
