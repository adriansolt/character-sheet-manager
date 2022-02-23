import { IXaracterEquippedArmor } from 'app/entities/xaracter-equipped-armor/xaracter-equipped-armor.model';
import { ArmorLocation } from 'app/entities/enumerations/armor-location.model';

export interface IArmorPiece {
  id?: number;
  location?: ArmorLocation | null;
  defenseModifier?: number | null;
  xaracterEquippedArmors?: IXaracterEquippedArmor[] | null;
}

export class ArmorPiece implements IArmorPiece {
  constructor(
    public id?: number,
    public location?: ArmorLocation | null,
    public defenseModifier?: number | null,
    public xaracterEquippedArmors?: IXaracterEquippedArmor[] | null
  ) {}
}

export function getArmorPieceIdentifier(armorPiece: IArmorPiece): number | undefined {
  return armorPiece.id;
}
