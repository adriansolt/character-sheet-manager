import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CharacterComponent } from '../list/character.component';
import { CharacterDetailComponent } from '../detail/character-detail.component';
import { CharacterUpdateComponent } from '../update/character-update.component';
import { CharacterRoutingResolveService } from './character-routing-resolve.service';

const characterRoute: Routes = [
  {
    path: '',
    component: CharacterComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CharacterDetailComponent,
    resolve: {
      character: CharacterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CharacterUpdateComponent,
    resolve: {
      character: CharacterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CharacterUpdateComponent,
    resolve: {
      character: CharacterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(characterRoute)],
  exports: [RouterModule],
})
export class CharacterRoutingModule {}
