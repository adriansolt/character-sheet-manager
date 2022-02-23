import { IWeaponManeuver } from 'app/entities/weapon-maneuver/weapon-maneuver.model';

export interface IManeuver {
  id?: number;
  name?: string;
  modifier?: number | null;
  description?: string;
  weaponManeuvers?: IWeaponManeuver[] | null;
}

export class Maneuver implements IManeuver {
  constructor(
    public id?: number,
    public name?: string,
    public modifier?: number | null,
    public description?: string,
    public weaponManeuvers?: IWeaponManeuver[] | null
  ) {}
}

export function getManeuverIdentifier(maneuver: IManeuver): number | undefined {
  return maneuver.id;
}
