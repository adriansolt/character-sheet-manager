import { ICharacterEquippedWeapon } from 'app/entities/character-equipped-weapon/character-equipped-weapon.model';
import { IWeaponManeuver } from 'app/entities/weapon-maneuver/weapon-maneuver.model';

export interface IWeapon {
  id?: number;
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
