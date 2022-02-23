import { IWeapon } from 'app/entities/weapon/weapon.model';
import { IManeuver } from 'app/entities/maneuver/maneuver.model';

export interface IWeaponManeuver {
  id?: number;
  weapon?: IWeapon | null;
  maneuver?: IManeuver | null;
}

export class WeaponManeuver implements IWeaponManeuver {
  constructor(public id?: number, public weapon?: IWeapon | null, public maneuver?: IManeuver | null) {}
}

export function getWeaponManeuverIdentifier(weaponManeuver: IWeaponManeuver): number | undefined {
  return weaponManeuver.id;
}
