import { ICharacterEquippedWeapon } from 'app/entities/character-equipped-weapon/character-equipped-weapon.model';
import { IWeaponManeuver } from 'app/entities/weapon-maneuver/weapon-maneuver.model';
import { ICampaign } from 'app/entities/campaign/campaign.model';
import { ICharacter } from 'app/entities/character/character.model';

export interface IWeapon {
  id?: number;
  name?: string;
  description?: string | null;
  weight?: number;
  quality?: number;
  pictureContentType?: string | null;
  picture?: string | null;
  reach?: number;
  baseDamage?: number;
  requiredST?: number;
  damageModifier?: number | null;
  characterEquippedWeapons?: ICharacterEquippedWeapon[] | null;
  weaponManeuvers?: IWeaponManeuver[] | null;
  campaign?: ICampaign | null;
  character?: ICharacter | null;
}

export class Weapon implements IWeapon {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public weight?: number,
    public quality?: number,
    public pictureContentType?: string | null,
    public picture?: string | null,
    public reach?: number,
    public baseDamage?: number,
    public requiredST?: number,
    public damageModifier?: number | null,
    public characterEquippedWeapons?: ICharacterEquippedWeapon[] | null,
    public weaponManeuvers?: IWeaponManeuver[] | null,
    public campaign?: ICampaign | null,
    public character?: ICharacter | null
  ) {}
}

export function getWeaponIdentifier(weapon: IWeapon): number | undefined {
  return weapon.id;
}
