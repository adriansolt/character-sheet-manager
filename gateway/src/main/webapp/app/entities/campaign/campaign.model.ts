import { ICampaignUser } from 'app/entities/campaign-user/campaign-user.model';

export interface ICampaign {
  id?: number;
  name?: string | null;
  description?: string | null;
  mapContentType?: string | null;
  map?: string | null;
  masterId?: number | null;
  campaignUsers?: ICampaignUser[] | null;
}

export class Campaign implements ICampaign {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public mapContentType?: string | null,
    public map?: string | null,
    public masterId?: number | null,
    public campaignUsers?: ICampaignUser[] | null
  ) {}
}

export function getCampaignIdentifier(campaign: ICampaign): number | undefined {
  return campaign.id;
}
