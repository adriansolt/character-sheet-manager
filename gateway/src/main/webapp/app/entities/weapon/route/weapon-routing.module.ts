import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WeaponComponent } from '../list/weapon.component';
import { WeaponDetailComponent } from '../detail/weapon-detail.component';
import { WeaponUpdateComponent } from '../update/weapon-update.component';
import { WeaponRoutingResolveService } from './weapon-routing-resolve.service';

const weaponRoute: Routes = [
  {
    path: '',
    component: WeaponComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WeaponDetailComponent,
    resolve: {
      weapon: WeaponRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WeaponUpdateComponent,
    resolve: {
      weapon: WeaponRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WeaponUpdateComponent,
    resolve: {
      weapon: WeaponRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(weaponRoute)],
  exports: [RouterModule],
})
export class WeaponRoutingModule {}
