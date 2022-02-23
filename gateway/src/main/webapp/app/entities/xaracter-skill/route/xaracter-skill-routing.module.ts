import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { XaracterSkillComponent } from '../list/xaracter-skill.component';
import { XaracterSkillDetailComponent } from '../detail/xaracter-skill-detail.component';
import { XaracterSkillUpdateComponent } from '../update/xaracter-skill-update.component';
import { XaracterSkillRoutingResolveService } from './xaracter-skill-routing-resolve.service';

const xaracterSkillRoute: Routes = [
  {
    path: '',
    component: XaracterSkillComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: XaracterSkillDetailComponent,
    resolve: {
      xaracterSkill: XaracterSkillRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: XaracterSkillUpdateComponent,
    resolve: {
      xaracterSkill: XaracterSkillRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: XaracterSkillUpdateComponent,
    resolve: {
      xaracterSkill: XaracterSkillRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(xaracterSkillRoute)],
  exports: [RouterModule],
})
export class XaracterSkillRoutingModule {}
