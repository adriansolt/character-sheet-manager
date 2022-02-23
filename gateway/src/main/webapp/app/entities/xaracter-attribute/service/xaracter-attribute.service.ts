import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IXaracterAttribute, getXaracterAttributeIdentifier } from '../xaracter-attribute.model';

export type EntityResponseType = HttpResponse<IXaracterAttribute>;
export type EntityArrayResponseType = HttpResponse<IXaracterAttribute[]>;

@Injectable({ providedIn: 'root' })
export class XaracterAttributeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/xaracter-attributes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(xaracterAttribute: IXaracterAttribute): Observable<EntityResponseType> {
    return this.http.post<IXaracterAttribute>(this.resourceUrl, xaracterAttribute, { observe: 'response' });
  }

  update(xaracterAttribute: IXaracterAttribute): Observable<EntityResponseType> {
    return this.http.put<IXaracterAttribute>(
      `${this.resourceUrl}/${getXaracterAttributeIdentifier(xaracterAttribute) as number}`,
      xaracterAttribute,
      { observe: 'response' }
    );
  }

  partialUpdate(xaracterAttribute: IXaracterAttribute): Observable<EntityResponseType> {
    return this.http.patch<IXaracterAttribute>(
      `${this.resourceUrl}/${getXaracterAttributeIdentifier(xaracterAttribute) as number}`,
      xaracterAttribute,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IXaracterAttribute>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IXaracterAttribute[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addXaracterAttributeToCollectionIfMissing(
    xaracterAttributeCollection: IXaracterAttribute[],
    ...xaracterAttributesToCheck: (IXaracterAttribute | null | undefined)[]
  ): IXaracterAttribute[] {
    const xaracterAttributes: IXaracterAttribute[] = xaracterAttributesToCheck.filter(isPresent);
    if (xaracterAttributes.length > 0) {
      const xaracterAttributeCollectionIdentifiers = xaracterAttributeCollection.map(
        xaracterAttributeItem => getXaracterAttributeIdentifier(xaracterAttributeItem)!
      );
      const xaracterAttributesToAdd = xaracterAttributes.filter(xaracterAttributeItem => {
        const xaracterAttributeIdentifier = getXaracterAttributeIdentifier(xaracterAttributeItem);
        if (xaracterAttributeIdentifier == null || xaracterAttributeCollectionIdentifiers.includes(xaracterAttributeIdentifier)) {
          return false;
        }
        xaracterAttributeCollectionIdentifiers.push(xaracterAttributeIdentifier);
        return true;
      });
      return [...xaracterAttributesToAdd, ...xaracterAttributeCollection];
    }
    return xaracterAttributeCollection;
  }
}
