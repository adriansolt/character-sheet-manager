import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IXaracterEquippedWeapon, getXaracterEquippedWeaponIdentifier } from '../xaracter-equipped-weapon.model';

export type EntityResponseType = HttpResponse<IXaracterEquippedWeapon>;
export type EntityArrayResponseType = HttpResponse<IXaracterEquippedWeapon[]>;

@Injectable({ providedIn: 'root' })
export class XaracterEquippedWeaponService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/xaracter-equipped-weapons');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(xaracterEquippedWeapon: IXaracterEquippedWeapon): Observable<EntityResponseType> {
    return this.http.post<IXaracterEquippedWeapon>(this.resourceUrl, xaracterEquippedWeapon, { observe: 'response' });
  }

  update(xaracterEquippedWeapon: IXaracterEquippedWeapon): Observable<EntityResponseType> {
    return this.http.put<IXaracterEquippedWeapon>(
      `${this.resourceUrl}/${getXaracterEquippedWeaponIdentifier(xaracterEquippedWeapon) as number}`,
      xaracterEquippedWeapon,
      { observe: 'response' }
    );
  }

  partialUpdate(xaracterEquippedWeapon: IXaracterEquippedWeapon): Observable<EntityResponseType> {
    return this.http.patch<IXaracterEquippedWeapon>(
      `${this.resourceUrl}/${getXaracterEquippedWeaponIdentifier(xaracterEquippedWeapon) as number}`,
      xaracterEquippedWeapon,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IXaracterEquippedWeapon>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IXaracterEquippedWeapon[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addXaracterEquippedWeaponToCollectionIfMissing(
    xaracterEquippedWeaponCollection: IXaracterEquippedWeapon[],
    ...xaracterEquippedWeaponsToCheck: (IXaracterEquippedWeapon | null | undefined)[]
  ): IXaracterEquippedWeapon[] {
    const xaracterEquippedWeapons: IXaracterEquippedWeapon[] = xaracterEquippedWeaponsToCheck.filter(isPresent);
    if (xaracterEquippedWeapons.length > 0) {
      const xaracterEquippedWeaponCollectionIdentifiers = xaracterEquippedWeaponCollection.map(
        xaracterEquippedWeaponItem => getXaracterEquippedWeaponIdentifier(xaracterEquippedWeaponItem)!
      );
      const xaracterEquippedWeaponsToAdd = xaracterEquippedWeapons.filter(xaracterEquippedWeaponItem => {
        const xaracterEquippedWeaponIdentifier = getXaracterEquippedWeaponIdentifier(xaracterEquippedWeaponItem);
        if (
          xaracterEquippedWeaponIdentifier == null ||
          xaracterEquippedWeaponCollectionIdentifiers.includes(xaracterEquippedWeaponIdentifier)
        ) {
          return false;
        }
        xaracterEquippedWeaponCollectionIdentifiers.push(xaracterEquippedWeaponIdentifier);
        return true;
      });
      return [...xaracterEquippedWeaponsToAdd, ...xaracterEquippedWeaponCollection];
    }
    return xaracterEquippedWeaponCollection;
  }
}
