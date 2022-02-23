import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IXaracterEquippedArmor, getXaracterEquippedArmorIdentifier } from '../xaracter-equipped-armor.model';

export type EntityResponseType = HttpResponse<IXaracterEquippedArmor>;
export type EntityArrayResponseType = HttpResponse<IXaracterEquippedArmor[]>;

@Injectable({ providedIn: 'root' })
export class XaracterEquippedArmorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/xaracter-equipped-armors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(xaracterEquippedArmor: IXaracterEquippedArmor): Observable<EntityResponseType> {
    return this.http.post<IXaracterEquippedArmor>(this.resourceUrl, xaracterEquippedArmor, { observe: 'response' });
  }

  update(xaracterEquippedArmor: IXaracterEquippedArmor): Observable<EntityResponseType> {
    return this.http.put<IXaracterEquippedArmor>(
      `${this.resourceUrl}/${getXaracterEquippedArmorIdentifier(xaracterEquippedArmor) as number}`,
      xaracterEquippedArmor,
      { observe: 'response' }
    );
  }

  partialUpdate(xaracterEquippedArmor: IXaracterEquippedArmor): Observable<EntityResponseType> {
    return this.http.patch<IXaracterEquippedArmor>(
      `${this.resourceUrl}/${getXaracterEquippedArmorIdentifier(xaracterEquippedArmor) as number}`,
      xaracterEquippedArmor,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IXaracterEquippedArmor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IXaracterEquippedArmor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addXaracterEquippedArmorToCollectionIfMissing(
    xaracterEquippedArmorCollection: IXaracterEquippedArmor[],
    ...xaracterEquippedArmorsToCheck: (IXaracterEquippedArmor | null | undefined)[]
  ): IXaracterEquippedArmor[] {
    const xaracterEquippedArmors: IXaracterEquippedArmor[] = xaracterEquippedArmorsToCheck.filter(isPresent);
    if (xaracterEquippedArmors.length > 0) {
      const xaracterEquippedArmorCollectionIdentifiers = xaracterEquippedArmorCollection.map(
        xaracterEquippedArmorItem => getXaracterEquippedArmorIdentifier(xaracterEquippedArmorItem)!
      );
      const xaracterEquippedArmorsToAdd = xaracterEquippedArmors.filter(xaracterEquippedArmorItem => {
        const xaracterEquippedArmorIdentifier = getXaracterEquippedArmorIdentifier(xaracterEquippedArmorItem);
        if (
          xaracterEquippedArmorIdentifier == null ||
          xaracterEquippedArmorCollectionIdentifiers.includes(xaracterEquippedArmorIdentifier)
        ) {
          return false;
        }
        xaracterEquippedArmorCollectionIdentifiers.push(xaracterEquippedArmorIdentifier);
        return true;
      });
      return [...xaracterEquippedArmorsToAdd, ...xaracterEquippedArmorCollection];
    }
    return xaracterEquippedArmorCollection;
  }
}
