import { ICharacter } from 'app/entities/character/character.model';
import { AttributeName } from 'app/entities/enumerations/attribute-name.model';

export interface ICharacterAttribute {
  id?: number;
  name?: AttributeName;
  points?: number;
  attributeModifier?: number | null;
  character?: ICharacter | null;
}

export class CharacterAttribute implements ICharacterAttribute {
  constructor(
    public id?: number,
    public name?: AttributeName,
    public points?: number,
    public attributeModifier?: number | null,
    public character?: ICharacter | null
  ) {}
}

export function getCharacterAttributeIdentifier(characterAttribute: ICharacterAttribute): number | undefined {
  return characterAttribute.id;
}
