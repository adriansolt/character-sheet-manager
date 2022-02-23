import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWeapon, getWeaponIdentifier } from '../weapon.model';

export type EntityResponseType = HttpResponse<IWeapon>;
export type EntityArrayResponseType = HttpResponse<IWeapon[]>;

@Injectable({ providedIn: 'root' })
export class WeaponService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/weapons');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(weapon: IWeapon): Observable<EntityResponseType> {
    return this.http.post<IWeapon>(this.resourceUrl, weapon, { observe: 'response' });
  }

  update(weapon: IWeapon): Observable<EntityResponseType> {
    return this.http.put<IWeapon>(`${this.resourceUrl}/${getWeaponIdentifier(weapon) as number}`, weapon, { observe: 'response' });
  }

  partialUpdate(weapon: IWeapon): Observable<EntityResponseType> {
    return this.http.patch<IWeapon>(`${this.resourceUrl}/${getWeaponIdentifier(weapon) as number}`, weapon, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWeapon>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWeapon[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWeaponToCollectionIfMissing(weaponCollection: IWeapon[], ...weaponsToCheck: (IWeapon | null | undefined)[]): IWeapon[] {
    const weapons: IWeapon[] = weaponsToCheck.filter(isPresent);
    if (weapons.length > 0) {
      const weaponCollectionIdentifiers = weaponCollection.map(weaponItem => getWeaponIdentifier(weaponItem)!);
      const weaponsToAdd = weapons.filter(weaponItem => {
        const weaponIdentifier = getWeaponIdentifier(weaponItem);
        if (weaponIdentifier == null || weaponCollectionIdentifiers.includes(weaponIdentifier)) {
          return false;
        }
        weaponCollectionIdentifiers.push(weaponIdentifier);
        return true;
      });
      return [...weaponsToAdd, ...weaponCollection];
    }
    return weaponCollection;
  }
}
