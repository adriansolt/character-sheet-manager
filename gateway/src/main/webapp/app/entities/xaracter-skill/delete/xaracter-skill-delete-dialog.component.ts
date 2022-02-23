import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IXaracterSkill } from '../xaracter-skill.model';
import { XaracterSkillService } from '../service/xaracter-skill.service';

@Component({
  templateUrl: './xaracter-skill-delete-dialog.component.html',
})
export class XaracterSkillDeleteDialogComponent {
  xaracterSkill?: IXaracterSkill;

  constructor(protected xaracterSkillService: XaracterSkillService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.xaracterSkillService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
