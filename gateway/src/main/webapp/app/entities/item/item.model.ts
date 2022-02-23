export interface IItem {
  id?: number;
  name?: string;
  description?: string | null;
  weight?: number;
  quality?: number;
  pictureContentType?: string | null;
  picture?: string | null;
  characterId?: number | null;
  campaignId?: number | null;
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
    public characterId?: number | null,
    public campaignId?: number | null
  ) {}
}

export function getItemIdentifier(item: IItem): number | undefined {
  return item.id;
}
