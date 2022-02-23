import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CharacterAttributeComponent } from '../list/character-attribute.component';
import { CharacterAttributeDetailComponent } from '../detail/character-attribute-detail.component';
import { CharacterAttributeUpdateComponent } from '../update/character-attribute-update.component';
import { CharacterAttributeRoutingResolveService } from './character-attribute-routing-resolve.service';

const characterAttributeRoute: Routes = [
  {
    path: '',
    component: CharacterAttributeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CharacterAttributeDetailComponent,
    resolve: {
      characterAttribute: CharacterAttributeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CharacterAttributeUpdateComponent,
    resolve: {
      characterAttribute: CharacterAttributeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CharacterAttributeUpdateComponent,
    resolve: {
      characterAttribute: CharacterAttributeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(characterAttributeRoute)],
  exports: [RouterModule],
})
export class CharacterAttributeRoutingModule {}
