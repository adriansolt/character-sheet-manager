import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { XaracterComponent } from '../list/xaracter.component';
import { XaracterDetailComponent } from '../detail/xaracter-detail.component';
import { XaracterUpdateComponent } from '../update/xaracter-update.component';
import { XaracterRoutingResolveService } from './xaracter-routing-resolve.service';

const xaracterRoute: Routes = [
  {
    path: '',
    component: XaracterComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: XaracterDetailComponent,
    resolve: {
      xaracter: XaracterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: XaracterUpdateComponent,
    resolve: {
      xaracter: XaracterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: XaracterUpdateComponent,
    resolve: {
      xaracter: XaracterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(xaracterRoute)],
  exports: [RouterModule],
})
export class XaracterRoutingModule {}
