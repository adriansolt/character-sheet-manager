import { ICampaign } from 'app/entities/campaign/campaign.model';
import { IUser } from 'app/entities/user/user.model';

export interface ICampaignUser {
  id?: number;
  campaign?: ICampaign;
  user?: IUser;
}

export class CampaignUser implements ICampaignUser {
  constructor(public id?: number, public campaign?: ICampaign, public user?: IUser) {}
}

export function getCampaignUserIdentifier(campaignUser: ICampaignUser): number | undefined {
  return campaignUser.id;
}
