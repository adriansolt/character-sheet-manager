import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICharacterSkill } from '../character-skill.model';
import { CharacterSkillService } from '../service/character-skill.service';

@Component({
  templateUrl: './character-skill-delete-dialog.component.html',
})
export class CharacterSkillDeleteDialogComponent {
  characterSkill?: ICharacterSkill;

  constructor(protected characterSkillService: CharacterSkillService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.characterSkillService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
