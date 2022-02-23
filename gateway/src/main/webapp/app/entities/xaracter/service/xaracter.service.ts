import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IXaracter, getXaracterIdentifier } from '../xaracter.model';

export type EntityResponseType = HttpResponse<IXaracter>;
export type EntityArrayResponseType = HttpResponse<IXaracter[]>;

@Injectable({ providedIn: 'root' })
export class XaracterService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/xaracters');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(xaracter: IXaracter): Observable<EntityResponseType> {
    return this.http.post<IXaracter>(this.resourceUrl, xaracter, { observe: 'response' });
  }

  update(xaracter: IXaracter): Observable<EntityResponseType> {
    return this.http.put<IXaracter>(`${this.resourceUrl}/${getXaracterIdentifier(xaracter) as number}`, xaracter, { observe: 'response' });
  }

  partialUpdate(xaracter: IXaracter): Observable<EntityResponseType> {
    return this.http.patch<IXaracter>(`${this.resourceUrl}/${getXaracterIdentifier(xaracter) as number}`, xaracter, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IXaracter>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IXaracter[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addXaracterToCollectionIfMissing(xaracterCollection: IXaracter[], ...xaractersToCheck: (IXaracter | null | undefined)[]): IXaracter[] {
    const xaracters: IXaracter[] = xaractersToCheck.filter(isPresent);
    if (xaracters.length > 0) {
      const xaracterCollectionIdentifiers = xaracterCollection.map(xaracterItem => getXaracterIdentifier(xaracterItem)!);
      const xaractersToAdd = xaracters.filter(xaracterItem => {
        const xaracterIdentifier = getXaracterIdentifier(xaracterItem);
        if (xaracterIdentifier == null || xaracterCollectionIdentifiers.includes(xaracterIdentifier)) {
          return false;
        }
        xaracterCollectionIdentifiers.push(xaracterIdentifier);
        return true;
      });
      return [...xaractersToAdd, ...xaracterCollection];
    }
    return xaracterCollection;
  }
}
