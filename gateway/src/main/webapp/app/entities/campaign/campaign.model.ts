import { ICharacter } from 'app/entities/character/character.model';
import { IItem } from 'app/entities/item/item.model';
import { IWeapon } from 'app/entities/weapon/weapon.model';
import { IArmorPiece } from 'app/entities/armor-piece/armor-piece.model';
import { ICampaignUser } from 'app/entities/campaign-user/campaign-user.model';

export interface ICampaign {
  id?: number;
  name?: string;
  description?: string | null;
  mapContentType?: string;
  map?: string;
  masterId?: number;
  characters?: ICharacter[] | null;
  items?: IItem[] | null;
  weapons?: IWeapon[] | null;
  armorPieces?: IArmorPiece[] | null;
  campaignUsers?: ICampaignUser[] | null;
}

export class Campaign implements ICampaign {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public mapContentType?: string,
    public map?: string,
    public masterId?: number,
    public characters?: ICharacter[] | null,
    public items?: IItem[] | null,
    public weapons?: IWeapon[] | null,
    public armorPieces?: IArmorPiece[] | null,
    public campaignUsers?: ICampaignUser[] | null
  ) {}
}

export function getCampaignIdentifier(campaign: ICampaign): number | undefined {
  return campaign.id;
}
