import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICharacterAttribute } from '../character-attribute.model';
import { CharacterAttributeService } from '../service/character-attribute.service';

@Component({
  templateUrl: './character-attribute-delete-dialog.component.html',
})
export class CharacterAttributeDeleteDialogComponent {
  characterAttribute?: ICharacterAttribute;

  constructor(protected characterAttributeService: CharacterAttributeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.characterAttributeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
