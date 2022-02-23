import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICharacterEquippedWeapon } from '../character-equipped-weapon.model';
import { CharacterEquippedWeaponService } from '../service/character-equipped-weapon.service';

@Component({
  templateUrl: './character-equipped-weapon-delete-dialog.component.html',
})
export class CharacterEquippedWeaponDeleteDialogComponent {
  characterEquippedWeapon?: ICharacterEquippedWeapon;

  constructor(protected characterEquippedWeaponService: CharacterEquippedWeaponService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.characterEquippedWeaponService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
