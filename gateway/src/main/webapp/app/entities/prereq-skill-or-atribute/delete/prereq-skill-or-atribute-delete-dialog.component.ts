import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrereqSkillOrAtribute } from '../prereq-skill-or-atribute.model';
import { PrereqSkillOrAtributeService } from '../service/prereq-skill-or-atribute.service';

@Component({
  templateUrl: './prereq-skill-or-atribute-delete-dialog.component.html',
})
export class PrereqSkillOrAtributeDeleteDialogComponent {
  prereqSkillOrAtribute?: IPrereqSkillOrAtribute;

  constructor(protected prereqSkillOrAtributeService: PrereqSkillOrAtributeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.prereqSkillOrAtributeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
