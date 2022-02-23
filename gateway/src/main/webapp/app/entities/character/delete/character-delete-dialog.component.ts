import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICharacter } from '../character.model';
import { CharacterService } from '../service/character.service';

@Component({
  templateUrl: './character-delete-dialog.component.html',
})
export class CharacterDeleteDialogComponent {
  character?: ICharacter;

  constructor(protected characterService: CharacterService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.characterService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
