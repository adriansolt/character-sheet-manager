import { ICharacterEquippedWeapon } from 'app/entities/character-equipped-weapon/character-equipped-weapon.model';
import { IWeaponManeuver } from 'app/entities/weapon-maneuver/weapon-maneuver.model';
import { IItem } from '../item/item.model';

export interface IWeapon extends IItem {
  reach?: number;
  baseDamage?: number;
  requiredST?: number;
  damageModifier?: number | null;
  characterEquippedWeapons?: ICharacterEquippedWeapon[] | null;
  weaponManeuvers?: IWeaponManeuver[] | null;
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
    public characterId?: number | null,
    public campaignId?: number | null,
    public reach?: number,
    public baseDamage?: number,
    public requiredST?: number,
    public damageModifier?: number | null,
    public characterEquippedWeapons?: ICharacterEquippedWeapon[] | null,
    public weaponManeuvers?: IWeaponManeuver[] | null
  ) {}
}

export function getWeaponIdentifier(weapon: IWeapon): number | undefined {
  return weapon.id;
}
