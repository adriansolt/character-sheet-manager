import { IWeapon } from 'app/entities/weapon/weapon.model';
import { IManeuver } from 'app/entities/maneuver/maneuver.model';

export interface IWeaponManeuver {
  id?: number;
  weaponId?: IWeapon | null;
  maneuverId?: IManeuver | null;
}

export class WeaponManeuver implements IWeaponManeuver {
  constructor(public id?: number, public weaponId?: IWeapon | null, public maneuverId?: IManeuver | null) {}
}

export function getWeaponManeuverIdentifier(weaponManeuver: IWeaponManeuver): number | undefined {
  return weaponManeuver.id;
}
