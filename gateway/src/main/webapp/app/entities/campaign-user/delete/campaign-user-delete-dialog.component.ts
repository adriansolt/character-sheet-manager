import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICampaignUser } from '../campaign-user.model';
import { CampaignUserService } from '../service/campaign-user.service';

@Component({
  templateUrl: './campaign-user-delete-dialog.component.html',
})
export class CampaignUserDeleteDialogComponent {
  campaignUser?: ICampaignUser;

  constructor(protected campaignUserService: CampaignUserService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.campaignUserService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
