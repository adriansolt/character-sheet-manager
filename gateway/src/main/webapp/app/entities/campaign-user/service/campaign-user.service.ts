import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICampaignUser, getCampaignUserIdentifier } from '../campaign-user.model';

export type EntityResponseType = HttpResponse<ICampaignUser>;
export type EntityArrayResponseType = HttpResponse<ICampaignUser[]>;

@Injectable({ providedIn: 'root' })
export class CampaignUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/campaign-users');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(campaignUser: ICampaignUser): Observable<EntityResponseType> {
    return this.http.post<ICampaignUser>(this.resourceUrl, campaignUser, { observe: 'response' });
  }

  update(campaignUser: ICampaignUser): Observable<EntityResponseType> {
    return this.http.put<ICampaignUser>(`${this.resourceUrl}/${getCampaignUserIdentifier(campaignUser) as number}`, campaignUser, {
      observe: 'response',
    });
  }

  partialUpdate(campaignUser: ICampaignUser): Observable<EntityResponseType> {
    return this.http.patch<ICampaignUser>(`${this.resourceUrl}/${getCampaignUserIdentifier(campaignUser) as number}`, campaignUser, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICampaignUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICampaignUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCampaignUserToCollectionIfMissing(
    campaignUserCollection: ICampaignUser[],
    ...campaignUsersToCheck: (ICampaignUser | null | undefined)[]
  ): ICampaignUser[] {
    const campaignUsers: ICampaignUser[] = campaignUsersToCheck.filter(isPresent);
    if (campaignUsers.length > 0) {
      const campaignUserCollectionIdentifiers = campaignUserCollection.map(
        campaignUserItem => getCampaignUserIdentifier(campaignUserItem)!
      );
      const campaignUsersToAdd = campaignUsers.filter(campaignUserItem => {
        const campaignUserIdentifier = getCampaignUserIdentifier(campaignUserItem);
        if (campaignUserIdentifier == null || campaignUserCollectionIdentifiers.includes(campaignUserIdentifier)) {
          return false;
        }
        campaignUserCollectionIdentifiers.push(campaignUserIdentifier);
        return true;
      });
      return [...campaignUsersToAdd, ...campaignUserCollection];
    }
    return campaignUserCollection;
  }
}
