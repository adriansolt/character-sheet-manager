import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IManeuver, getManeuverIdentifier } from '../maneuver.model';

export type EntityResponseType = HttpResponse<IManeuver>;
export type EntityArrayResponseType = HttpResponse<IManeuver[]>;

@Injectable({ providedIn: 'root' })
export class ManeuverService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/maneuvers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(maneuver: IManeuver): Observable<EntityResponseType> {
    return this.http.post<IManeuver>(this.resourceUrl, maneuver, { observe: 'response' });
  }

  update(maneuver: IManeuver): Observable<EntityResponseType> {
    return this.http.put<IManeuver>(`${this.resourceUrl}/${getManeuverIdentifier(maneuver) as number}`, maneuver, { observe: 'response' });
  }

  partialUpdate(maneuver: IManeuver): Observable<EntityResponseType> {
    return this.http.patch<IManeuver>(`${this.resourceUrl}/${getManeuverIdentifier(maneuver) as number}`, maneuver, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IManeuver>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IManeuver[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addManeuverToCollectionIfMissing(maneuverCollection: IManeuver[], ...maneuversToCheck: (IManeuver | null | undefined)[]): IManeuver[] {
    const maneuvers: IManeuver[] = maneuversToCheck.filter(isPresent);
    if (maneuvers.length > 0) {
      const maneuverCollectionIdentifiers = maneuverCollection.map(maneuverItem => getManeuverIdentifier(maneuverItem)!);
      const maneuversToAdd = maneuvers.filter(maneuverItem => {
        const maneuverIdentifier = getManeuverIdentifier(maneuverItem);
        if (maneuverIdentifier == null || maneuverCollectionIdentifiers.includes(maneuverIdentifier)) {
          return false;
        }
        maneuverCollectionIdentifiers.push(maneuverIdentifier);
        return true;
      });
      return [...maneuversToAdd, ...maneuverCollection];
    }
    return maneuverCollection;
  }
}
