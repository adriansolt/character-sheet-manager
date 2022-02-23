import { IXaracter } from 'app/entities/xaracter/xaracter.model';
import { AttributeName } from 'app/entities/enumerations/attribute-name.model';

export interface IXaracterAttribute {
  id?: number;
  name?: AttributeName;
  points?: number;
  attributeModifier?: number | null;
  xaracterId?: IXaracter | null;
}

export class XaracterAttribute implements IXaracterAttribute {
  constructor(
    public id?: number,
    public name?: AttributeName,
    public points?: number,
    public attributeModifier?: number | null,
    public xaracterId?: IXaracter | null
  ) {}
}

export function getXaracterAttributeIdentifier(xaracterAttribute: IXaracterAttribute): number | undefined {
  return xaracterAttribute.id;
}
