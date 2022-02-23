import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CharacterEquippedWeaponComponent } from '../list/character-equipped-weapon.component';
import { CharacterEquippedWeaponDetailComponent } from '../detail/character-equipped-weapon-detail.component';
import { CharacterEquippedWeaponUpdateComponent } from '../update/character-equipped-weapon-update.component';
import { CharacterEquippedWeaponRoutingResolveService } from './character-equipped-weapon-routing-resolve.service';

const characterEquippedWeaponRoute: Routes = [
  {
    path: '',
    component: CharacterEquippedWeaponComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CharacterEquippedWeaponDetailComponent,
    resolve: {
      characterEquippedWeapon: CharacterEquippedWeaponRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CharacterEquippedWeaponUpdateComponent,
    resolve: {
      characterEquippedWeapon: CharacterEquippedWeaponRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CharacterEquippedWeaponUpdateComponent,
    resolve: {
      characterEquippedWeapon: CharacterEquippedWeaponRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(characterEquippedWeaponRoute)],
  exports: [RouterModule],
})
export class CharacterEquippedWeaponRoutingModule {}
