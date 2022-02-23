import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CharacterSkillComponent } from '../list/character-skill.component';
import { CharacterSkillDetailComponent } from '../detail/character-skill-detail.component';
import { CharacterSkillUpdateComponent } from '../update/character-skill-update.component';
import { CharacterSkillRoutingResolveService } from './character-skill-routing-resolve.service';

const characterSkillRoute: Routes = [
  {
    path: '',
    component: CharacterSkillComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CharacterSkillDetailComponent,
    resolve: {
      characterSkill: CharacterSkillRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CharacterSkillUpdateComponent,
    resolve: {
      characterSkill: CharacterSkillRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CharacterSkillUpdateComponent,
    resolve: {
      characterSkill: CharacterSkillRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(characterSkillRoute)],
  exports: [RouterModule],
})
export class CharacterSkillRoutingModule {}
