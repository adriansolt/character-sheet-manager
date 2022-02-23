import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IManeuver, Maneuver } from '../maneuver.model';
import { ManeuverService } from '../service/maneuver.service';

@Injectable({ providedIn: 'root' })
export class ManeuverRoutingResolveService implements Resolve<IManeuver> {
  constructor(protected service: ManeuverService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IManeuver> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((maneuver: HttpResponse<Maneuver>) => {
          if (maneuver.body) {
            return of(maneuver.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Maneuver());
  }
}
