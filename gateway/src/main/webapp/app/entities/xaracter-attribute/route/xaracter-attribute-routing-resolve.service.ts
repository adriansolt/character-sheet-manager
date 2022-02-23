import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IXaracterAttribute, XaracterAttribute } from '../xaracter-attribute.model';
import { XaracterAttributeService } from '../service/xaracter-attribute.service';

@Injectable({ providedIn: 'root' })
export class XaracterAttributeRoutingResolveService implements Resolve<IXaracterAttribute> {
  constructor(protected service: XaracterAttributeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IXaracterAttribute> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((xaracterAttribute: HttpResponse<XaracterAttribute>) => {
          if (xaracterAttribute.body) {
            return of(xaracterAttribute.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new XaracterAttribute());
  }
}
