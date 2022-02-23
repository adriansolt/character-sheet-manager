import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IXaracter } from '../xaracter.model';
import { XaracterService } from '../service/xaracter.service';

@Component({
  templateUrl: './xaracter-delete-dialog.component.html',
})
export class XaracterDeleteDialogComponent {
  xaracter?: IXaracter;

  constructor(protected xaracterService: XaracterService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.xaracterService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
