import { ICampaignUser } from 'app/entities/campaign-user/campaign-user.model';
import { ICharacter } from 'app/entities/character/character.model';
import { IItem } from 'app/entities/item/item.model';
import { IWeapon } from 'app/entities/weapon/weapon.model';
import { IArmorPiece } from 'app/entities/armor-piece/armor-piece.model';

export interface ICampaign {
  id?: number;
  name?: string | null;
  description?: string | null;
  mapContentType?: string | null;
  map?: string | null;
  masterId?: number | null;
  campaignUsers?: ICampaignUser[] | null;
  characters?: ICharacter[] | null;
  items?: IItem[] | null;
  weapons?: IWeapon[] | null;
  armorPieces?: IArmorPiece[] | null;
}

export class Campaign implements ICampaign {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public mapContentType?: string | null,
    public map?: string | null,
    public masterId?: number | null,
    public campaignUsers?: ICampaignUser[] | null,
    public characters?: ICharacter[] | null,
    public items?: IItem[] | null,
    public weapons?: IWeapon[] | null,
    public armorPieces?: IArmorPiece[] | null
  ) {}
}

export function getCampaignIdentifier(campaign: ICampaign): number | undefined {
  return campaign.id;
}
