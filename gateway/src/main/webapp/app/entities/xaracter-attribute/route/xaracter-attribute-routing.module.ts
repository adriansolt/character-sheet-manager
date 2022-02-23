import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { XaracterAttributeComponent } from '../list/xaracter-attribute.component';
import { XaracterAttributeDetailComponent } from '../detail/xaracter-attribute-detail.component';
import { XaracterAttributeUpdateComponent } from '../update/xaracter-attribute-update.component';
import { XaracterAttributeRoutingResolveService } from './xaracter-attribute-routing-resolve.service';

const xaracterAttributeRoute: Routes = [
  {
    path: '',
    component: XaracterAttributeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: XaracterAttributeDetailComponent,
    resolve: {
      xaracterAttribute: XaracterAttributeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: XaracterAttributeUpdateComponent,
    resolve: {
      xaracterAttribute: XaracterAttributeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: XaracterAttributeUpdateComponent,
    resolve: {
      xaracterAttribute: XaracterAttributeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(xaracterAttributeRoute)],
  exports: [RouterModule],
})
export class XaracterAttributeRoutingModule {}
