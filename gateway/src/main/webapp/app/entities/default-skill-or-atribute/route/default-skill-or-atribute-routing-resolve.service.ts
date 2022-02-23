import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDefaultSkillOrAtribute, DefaultSkillOrAtribute } from '../default-skill-or-atribute.model';
import { DefaultSkillOrAtributeService } from '../service/default-skill-or-atribute.service';

@Injectable({ providedIn: 'root' })
export class DefaultSkillOrAtributeRoutingResolveService implements Resolve<IDefaultSkillOrAtribute> {
  constructor(protected service: DefaultSkillOrAtributeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDefaultSkillOrAtribute> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((defaultSkillOrAtribute: HttpResponse<DefaultSkillOrAtribute>) => {
          if (defaultSkillOrAtribute.body) {
            return of(defaultSkillOrAtribute.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DefaultSkillOrAtribute());
  }
}
