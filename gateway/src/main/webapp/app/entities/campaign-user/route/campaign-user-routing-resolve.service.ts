import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICampaignUser, CampaignUser } from '../campaign-user.model';
import { CampaignUserService } from '../service/campaign-user.service';

@Injectable({ providedIn: 'root' })
export class CampaignUserRoutingResolveService implements Resolve<ICampaignUser> {
  constructor(protected service: CampaignUserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICampaignUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((campaignUser: HttpResponse<CampaignUser>) => {
          if (campaignUser.body) {
            return of(campaignUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CampaignUser());
  }
}
