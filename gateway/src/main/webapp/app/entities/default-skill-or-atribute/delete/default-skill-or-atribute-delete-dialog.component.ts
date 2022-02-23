import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDefaultSkillOrAtribute } from '../default-skill-or-atribute.model';
import { DefaultSkillOrAtributeService } from '../service/default-skill-or-atribute.service';

@Component({
  templateUrl: './default-skill-or-atribute-delete-dialog.component.html',
})
export class DefaultSkillOrAtributeDeleteDialogComponent {
  defaultSkillOrAtribute?: IDefaultSkillOrAtribute;

  constructor(protected defaultSkillOrAtributeService: DefaultSkillOrAtributeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.defaultSkillOrAtributeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
