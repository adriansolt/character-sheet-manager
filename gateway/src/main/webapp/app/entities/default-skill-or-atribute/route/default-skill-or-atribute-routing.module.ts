import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DefaultSkillOrAtributeComponent } from '../list/default-skill-or-atribute.component';
import { DefaultSkillOrAtributeDetailComponent } from '../detail/default-skill-or-atribute-detail.component';
import { DefaultSkillOrAtributeUpdateComponent } from '../update/default-skill-or-atribute-update.component';
import { DefaultSkillOrAtributeRoutingResolveService } from './default-skill-or-atribute-routing-resolve.service';

const defaultSkillOrAtributeRoute: Routes = [
  {
    path: '',
    component: DefaultSkillOrAtributeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DefaultSkillOrAtributeDetailComponent,
    resolve: {
      defaultSkillOrAtribute: DefaultSkillOrAtributeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DefaultSkillOrAtributeUpdateComponent,
    resolve: {
      defaultSkillOrAtribute: DefaultSkillOrAtributeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DefaultSkillOrAtributeUpdateComponent,
    resolve: {
      defaultSkillOrAtribute: DefaultSkillOrAtributeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(defaultSkillOrAtributeRoute)],
  exports: [RouterModule],
})
export class DefaultSkillOrAtributeRoutingModule {}
