import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ManeuverComponent } from '../list/maneuver.component';
import { ManeuverDetailComponent } from '../detail/maneuver-detail.component';
import { ManeuverUpdateComponent } from '../update/maneuver-update.component';
import { ManeuverRoutingResolveService } from './maneuver-routing-resolve.service';

const maneuverRoute: Routes = [
  {
    path: '',
    component: ManeuverComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ManeuverDetailComponent,
    resolve: {
      maneuver: ManeuverRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ManeuverUpdateComponent,
    resolve: {
      maneuver: ManeuverRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ManeuverUpdateComponent,
    resolve: {
      maneuver: ManeuverRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(maneuverRoute)],
  exports: [RouterModule],
})
export class ManeuverRoutingModule {}
