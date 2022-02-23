import { IWeapon } from 'app/entities/weapon/weapon.model';
import { Handedness } from 'app/entities/enumerations/handedness.model';

export interface IXaracterEquippedWeapon {
  id?: number;
  xaracterId?: number | null;
  hand?: Handedness | null;
  weaponId?: IWeapon | null;
}

export class XaracterEquippedWeapon implements IXaracterEquippedWeapon {
  constructor(public id?: number, public xaracterId?: number | null, public hand?: Handedness | null, public weaponId?: IWeapon | null) {}
}

export function getXaracterEquippedWeaponIdentifier(xaracterEquippedWeapon: IXaracterEquippedWeapon): number | undefined {
  return xaracterEquippedWeapon.id;
}
