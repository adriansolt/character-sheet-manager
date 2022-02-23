import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrereqSkillOrAtribute, PrereqSkillOrAtribute } from '../prereq-skill-or-atribute.model';
import { PrereqSkillOrAtributeService } from '../service/prereq-skill-or-atribute.service';

@Injectable({ providedIn: 'root' })
export class PrereqSkillOrAtributeRoutingResolveService implements Resolve<IPrereqSkillOrAtribute> {
  constructor(protected service: PrereqSkillOrAtributeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrereqSkillOrAtribute> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((prereqSkillOrAtribute: HttpResponse<PrereqSkillOrAtribute>) => {
          if (prereqSkillOrAtribute.body) {
            return of(prereqSkillOrAtribute.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PrereqSkillOrAtribute());
  }
}
