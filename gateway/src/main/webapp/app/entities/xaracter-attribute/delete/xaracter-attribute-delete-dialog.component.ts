import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IXaracterAttribute } from '../xaracter-attribute.model';
import { XaracterAttributeService } from '../service/xaracter-attribute.service';

@Component({
  templateUrl: './xaracter-attribute-delete-dialog.component.html',
})
export class XaracterAttributeDeleteDialogComponent {
  xaracterAttribute?: IXaracterAttribute;

  constructor(protected xaracterAttributeService: XaracterAttributeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.xaracterAttributeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
