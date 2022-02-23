import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CampaignUserComponent } from './list/campaign-user.component';
import { CampaignUserDetailComponent } from './detail/campaign-user-detail.component';
import { CampaignUserUpdateComponent } from './update/campaign-user-update.component';
import { CampaignUserDeleteDialogComponent } from './delete/campaign-user-delete-dialog.component';
import { CampaignUserRoutingModule } from './route/campaign-user-routing.module';

@NgModule({
  imports: [SharedModule, CampaignUserRoutingModule],
  declarations: [CampaignUserComponent, CampaignUserDetailComponent, CampaignUserUpdateComponent, CampaignUserDeleteDialogComponent],
  entryComponents: [CampaignUserDeleteDialogComponent],
})
export class CampaignUserModule {}
