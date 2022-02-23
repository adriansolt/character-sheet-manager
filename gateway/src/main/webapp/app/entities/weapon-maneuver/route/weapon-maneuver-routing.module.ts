import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WeaponManeuverComponent } from '../list/weapon-maneuver.component';
import { WeaponManeuverDetailComponent } from '../detail/weapon-maneuver-detail.component';
import { WeaponManeuverUpdateComponent } from '../update/weapon-maneuver-update.component';
import { WeaponManeuverRoutingResolveService } from './weapon-maneuver-routing-resolve.service';

const weaponManeuverRoute: Routes = [
  {
    path: '',
    component: WeaponManeuverComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WeaponManeuverDetailComponent,
    resolve: {
      weaponManeuver: WeaponManeuverRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WeaponManeuverUpdateComponent,
    resolve: {
      weaponManeuver: WeaponManeuverRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WeaponManeuverUpdateComponent,
    resolve: {
      weaponManeuver: WeaponManeuverRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(weaponManeuverRoute)],
  exports: [RouterModule],
})
export class WeaponManeuverRoutingModule {}
