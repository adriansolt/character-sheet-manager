import { ICampaign } from 'app/entities/campaign/campaign.model';
import { ICharacter } from 'app/entities/character/character.model';

export interface IItem {
  id?: number;
  name?: string;
  description?: string | null;
  weight?: number;
  quality?: number;
  pictureContentType?: string | null;
  picture?: string | null;
  campaign?: ICampaign | null;
  character?: ICharacter | null;
}

export class Item implements IItem {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public weight?: number,
    public quality?: number,
    public pictureContentType?: string | null,
    public picture?: string | null,
    public campaign?: ICampaign | null,
    public character?: ICharacter | null
  ) {}
}

export function getItemIdentifier(item: IItem): number | undefined {
  return item.id;
}
