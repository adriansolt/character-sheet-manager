package com.adi.cms.gateway.service.dto;

import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.adi.cms.gateway.domain.Weapon} entity.
 */
public class WeaponDTO extends ItemDTO {

    @NotNull(message = "must not be null")
    private Integer reach;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    private Integer baseDamage;

    @NotNull(message = "must not be null")
    @Min(value = 1)
    private Integer requiredST;

    private Integer damageModifier;

    public Integer getReach() {
        return reach;
    }

    public void setReach(Integer reach) {
        this.reach = reach;
    }

    public Integer getBaseDamage() {
        return baseDamage;
    }

    public void setBaseDamage(Integer baseDamage) {
        this.baseDamage = baseDamage;
    }

    public Integer getRequiredST() {
        return requiredST;
    }

    public void setRequiredST(Integer requiredST) {
        this.requiredST = requiredST;
    }

    public Integer getDamageModifier() {
        return damageModifier;
    }

    public void setDamageModifier(Integer damageModifier) {
        this.damageModifier = damageModifier;
    }

    public CharacterDTO getCharacter() {
        return character;
    }

    public void setCharacter(CharacterDTO character) {
        this.character = character;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WeaponDTO)) {
            return false;
        }

        WeaponDTO weaponDTO = (WeaponDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, weaponDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WeaponDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", weight=" + getWeight() +
            ", quality=" + getQuality() +
            ", picture='" + getPicture() + "'" +
            ", reach=" + getReach() +
            ", baseDamage=" + getBaseDamage() +
            ", requiredST=" + getRequiredST() +
            ", damageModifier=" + getDamageModifier() +
            ", character=" + getCharacter() +
            "}";
    }
}
