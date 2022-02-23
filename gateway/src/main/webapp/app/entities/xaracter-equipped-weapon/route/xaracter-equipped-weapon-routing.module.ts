import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { XaracterEquippedWeaponComponent } from '../list/xaracter-equipped-weapon.component';
import { XaracterEquippedWeaponDetailComponent } from '../detail/xaracter-equipped-weapon-detail.component';
import { XaracterEquippedWeaponUpdateComponent } from '../update/xaracter-equipped-weapon-update.component';
import { XaracterEquippedWeaponRoutingResolveService } from './xaracter-equipped-weapon-routing-resolve.service';

const xaracterEquippedWeaponRoute: Routes = [
  {
    path: '',
    component: XaracterEquippedWeaponComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: XaracterEquippedWeaponDetailComponent,
    resolve: {
      xaracterEquippedWeapon: XaracterEquippedWeaponRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: XaracterEquippedWeaponUpdateComponent,
    resolve: {
      xaracterEquippedWeapon: XaracterEquippedWeaponRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: XaracterEquippedWeaponUpdateComponent,
    resolve: {
      xaracterEquippedWeapon: XaracterEquippedWeaponRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(xaracterEquippedWeaponRoute)],
  exports: [RouterModule],
})
export class XaracterEquippedWeaponRoutingModule {}
