import { IWeapon } from 'app/entities/weapon/weapon.model';
import { Handedness } from 'app/entities/enumerations/handedness.model';

export interface ICharacterEquippedWeapon {
  id?: number;
  hand?: Handedness | null;
  weapon?: IWeapon | null;
}

export class CharacterEquippedWeapon implements ICharacterEquippedWeapon {
  constructor(public id?: number, public hand?: Handedness | null, public weapon?: IWeapon | null) {}
}

export function getCharacterEquippedWeaponIdentifier(characterEquippedWeapon: ICharacterEquippedWeapon): number | undefined {
  return characterEquippedWeapon.id;
}
