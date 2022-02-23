import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CharacterEquippedArmorComponent } from '../list/character-equipped-armor.component';
import { CharacterEquippedArmorDetailComponent } from '../detail/character-equipped-armor-detail.component';
import { CharacterEquippedArmorUpdateComponent } from '../update/character-equipped-armor-update.component';
import { CharacterEquippedArmorRoutingResolveService } from './character-equipped-armor-routing-resolve.service';

const characterEquippedArmorRoute: Routes = [
  {
    path: '',
    component: CharacterEquippedArmorComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CharacterEquippedArmorDetailComponent,
    resolve: {
      characterEquippedArmor: CharacterEquippedArmorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CharacterEquippedArmorUpdateComponent,
    resolve: {
      characterEquippedArmor: CharacterEquippedArmorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CharacterEquippedArmorUpdateComponent,
    resolve: {
      characterEquippedArmor: CharacterEquippedArmorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(characterEquippedArmorRoute)],
  exports: [RouterModule],
})
export class CharacterEquippedArmorRoutingModule {}
