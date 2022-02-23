import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PrereqSkillOrAtributeComponent } from '../list/prereq-skill-or-atribute.component';
import { PrereqSkillOrAtributeDetailComponent } from '../detail/prereq-skill-or-atribute-detail.component';
import { PrereqSkillOrAtributeUpdateComponent } from '../update/prereq-skill-or-atribute-update.component';
import { PrereqSkillOrAtributeRoutingResolveService } from './prereq-skill-or-atribute-routing-resolve.service';

const prereqSkillOrAtributeRoute: Routes = [
  {
    path: '',
    component: PrereqSkillOrAtributeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PrereqSkillOrAtributeDetailComponent,
    resolve: {
      prereqSkillOrAtribute: PrereqSkillOrAtributeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PrereqSkillOrAtributeUpdateComponent,
    resolve: {
      prereqSkillOrAtribute: PrereqSkillOrAtributeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PrereqSkillOrAtributeUpdateComponent,
    resolve: {
      prereqSkillOrAtribute: PrereqSkillOrAtributeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(prereqSkillOrAtributeRoute)],
  exports: [RouterModule],
})
export class PrereqSkillOrAtributeRoutingModule {}
