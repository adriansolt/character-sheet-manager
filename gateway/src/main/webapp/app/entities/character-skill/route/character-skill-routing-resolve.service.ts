import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICharacterSkill, CharacterSkill } from '../character-skill.model';
import { CharacterSkillService } from '../service/character-skill.service';

@Injectable({ providedIn: 'root' })
export class CharacterSkillRoutingResolveService implements Resolve<ICharacterSkill> {
  constructor(protected service: CharacterSkillService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICharacterSkill> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((characterSkill: HttpResponse<CharacterSkill>) => {
          if (characterSkill.body) {
            return of(characterSkill.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CharacterSkill());
  }
}
