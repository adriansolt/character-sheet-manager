import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CampaignUserComponent } from '../list/campaign-user.component';
import { CampaignUserDetailComponent } from '../detail/campaign-user-detail.component';
import { CampaignUserUpdateComponent } from '../update/campaign-user-update.component';
import { CampaignUserRoutingResolveService } from './campaign-user-routing-resolve.service';

const campaignUserRoute: Routes = [
  {
    path: '',
    component: CampaignUserComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CampaignUserDetailComponent,
    resolve: {
      campaignUser: CampaignUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CampaignUserUpdateComponent,
    resolve: {
      campaignUser: CampaignUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CampaignUserUpdateComponent,
    resolve: {
      campaignUser: CampaignUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(campaignUserRoute)],
  exports: [RouterModule],
})
export class CampaignUserRoutingModule {}
