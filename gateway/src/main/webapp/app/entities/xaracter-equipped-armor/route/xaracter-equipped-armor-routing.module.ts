import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { XaracterEquippedArmorComponent } from '../list/xaracter-equipped-armor.component';
import { XaracterEquippedArmorDetailComponent } from '../detail/xaracter-equipped-armor-detail.component';
import { XaracterEquippedArmorUpdateComponent } from '../update/xaracter-equipped-armor-update.component';
import { XaracterEquippedArmorRoutingResolveService } from './xaracter-equipped-armor-routing-resolve.service';

const xaracterEquippedArmorRoute: Routes = [
  {
    path: '',
    component: XaracterEquippedArmorComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: XaracterEquippedArmorDetailComponent,
    resolve: {
      xaracterEquippedArmor: XaracterEquippedArmorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: XaracterEquippedArmorUpdateComponent,
    resolve: {
      xaracterEquippedArmor: XaracterEquippedArmorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: XaracterEquippedArmorUpdateComponent,
    resolve: {
      xaracterEquippedArmor: XaracterEquippedArmorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(xaracterEquippedArmorRoute)],
  exports: [RouterModule],
})
export class XaracterEquippedArmorRoutingModule {}
