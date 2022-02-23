import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICharacterEquippedArmor } from '../character-equipped-armor.model';
import { CharacterEquippedArmorService } from '../service/character-equipped-armor.service';

@Component({
  templateUrl: './character-equipped-armor-delete-dialog.component.html',
})
export class CharacterEquippedArmorDeleteDialogComponent {
  characterEquippedArmor?: ICharacterEquippedArmor;

  constructor(protected characterEquippedArmorService: CharacterEquippedArmorService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.characterEquippedArmorService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
