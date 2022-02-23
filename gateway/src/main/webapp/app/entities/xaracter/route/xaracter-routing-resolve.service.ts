import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IXaracter, Xaracter } from '../xaracter.model';
import { XaracterService } from '../service/xaracter.service';

@Injectable({ providedIn: 'root' })
export class XaracterRoutingResolveService implements Resolve<IXaracter> {
  constructor(protected service: XaracterService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IXaracter> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((xaracter: HttpResponse<Xaracter>) => {
          if (xaracter.body) {
            return of(xaracter.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Xaracter());
  }
}
