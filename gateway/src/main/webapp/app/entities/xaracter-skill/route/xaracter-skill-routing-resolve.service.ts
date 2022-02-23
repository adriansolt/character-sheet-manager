import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IXaracterSkill, XaracterSkill } from '../xaracter-skill.model';
import { XaracterSkillService } from '../service/xaracter-skill.service';

@Injectable({ providedIn: 'root' })
export class XaracterSkillRoutingResolveService implements Resolve<IXaracterSkill> {
  constructor(protected service: XaracterSkillService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IXaracterSkill> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((xaracterSkill: HttpResponse<XaracterSkill>) => {
          if (xaracterSkill.body) {
            return of(xaracterSkill.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new XaracterSkill());
  }
}
