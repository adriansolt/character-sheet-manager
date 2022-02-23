import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWeaponManeuver, getWeaponManeuverIdentifier } from '../weapon-maneuver.model';

export type EntityResponseType = HttpResponse<IWeaponManeuver>;
export type EntityArrayResponseType = HttpResponse<IWeaponManeuver[]>;

@Injectable({ providedIn: 'root' })
export class WeaponManeuverService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/weapon-maneuvers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(weaponManeuver: IWeaponManeuver): Observable<EntityResponseType> {
    return this.http.post<IWeaponManeuver>(this.resourceUrl, weaponManeuver, { observe: 'response' });
  }

  update(weaponManeuver: IWeaponManeuver): Observable<EntityResponseType> {
    return this.http.put<IWeaponManeuver>(`${this.resourceUrl}/${getWeaponManeuverIdentifier(weaponManeuver) as number}`, weaponManeuver, {
      observe: 'response',
    });
  }

  partialUpdate(weaponManeuver: IWeaponManeuver): Observable<EntityResponseType> {
    return this.http.patch<IWeaponManeuver>(
      `${this.resourceUrl}/${getWeaponManeuverIdentifier(weaponManeuver) as number}`,
      weaponManeuver,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWeaponManeuver>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWeaponManeuver[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWeaponManeuverToCollectionIfMissing(
    weaponManeuverCollection: IWeaponManeuver[],
    ...weaponManeuversToCheck: (IWeaponManeuver | null | undefined)[]
  ): IWeaponManeuver[] {
    const weaponManeuvers: IWeaponManeuver[] = weaponManeuversToCheck.filter(isPresent);
    if (weaponManeuvers.length > 0) {
      const weaponManeuverCollectionIdentifiers = weaponManeuverCollection.map(
        weaponManeuverItem => getWeaponManeuverIdentifier(weaponManeuverItem)!
      );
      const weaponManeuversToAdd = weaponManeuvers.filter(weaponManeuverItem => {
        const weaponManeuverIdentifier = getWeaponManeuverIdentifier(weaponManeuverItem);
        if (weaponManeuverIdentifier == null || weaponManeuverCollectionIdentifiers.includes(weaponManeuverIdentifier)) {
          return false;
        }
        weaponManeuverCollectionIdentifiers.push(weaponManeuverIdentifier);
        return true;
      });
      return [...weaponManeuversToAdd, ...weaponManeuverCollection];
    }
    return weaponManeuverCollection;
  }
}
