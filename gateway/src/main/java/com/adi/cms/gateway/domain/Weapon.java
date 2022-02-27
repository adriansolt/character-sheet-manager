package com.adi.cms.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Weapon.
 */
@Table("weapon")
@PrimaryKeyJoinColumn(name="id")
public class Weapon extends Item {

    @NotNull(message = "must not be null")
    @Column("reach")
    private Integer reach;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    @Column("base_damage")
    private Integer baseDamage;

    @NotNull(message = "must not be null")
    @Min(value = 1)
    @Column("required_st")
    private Integer requiredST;

    @Column("damage_modifier")
    private Integer damageModifier;

    @Transient
    @JsonIgnoreProperties(value = { "weapon" }, allowSetters = true)
    private Set<CharacterEquippedWeapon> characterEquippedWeapons = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "weapon", "maneuver" }, allowSetters = true)
    private Set<WeaponManeuver> weaponManeuvers = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(
        value = { "notes", "characterAttributes", "characterSkills", "items", "weapons", "armorPieces", "user", "campaign" },
        allowSetters = true
    )
    private Character character;

    @Column("character_id")
    private Long characterId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Weapon id(Long id) {
        this.setId(id);
        return this;
    }

    public Weapon name(String name) {
        this.setName(name);
        return this;
    }

    public Weapon description(String description) {
        this.setDescription(description);
        return this;
    }

    public Weapon weight(Integer weight) {
        this.setWeight(weight);
        return this;
    }

    public Weapon quality(Integer quality) {
        this.setQuality(quality);
        return this;
    }

    public Weapon picture(byte[] picture) {
        this.setPicture(picture);
        return this;
    }

    public Weapon pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public Weapon characterId(Long characterId) {
        this.setCharacterId(characterId);
        return this;
    }

    public Integer getReach() {
        return this.reach;
    }

    public Weapon reach(Integer reach) {
        this.setReach(reach);
        return this;
    }

    public void setReach(Integer reach) {
        this.reach = reach;
    }

    public Integer getBaseDamage() {
        return this.baseDamage;
    }

    public Weapon baseDamage(Integer baseDamage) {
        this.setBaseDamage(baseDamage);
        return this;
    }

    public void setBaseDamage(Integer baseDamage) {
        this.baseDamage = baseDamage;
    }

    public Integer getRequiredST() {
        return this.requiredST;
    }

    public Weapon requiredST(Integer requiredST) {
        this.setRequiredST(requiredST);
        return this;
    }

    public void setRequiredST(Integer requiredST) {
        this.requiredST = requiredST;
    }

    public Integer getDamageModifier() {
        return this.damageModifier;
    }

    public Weapon damageModifier(Integer damageModifier) {
        this.setDamageModifier(damageModifier);
        return this;
    }

    public void setDamageModifier(Integer damageModifier) {
        this.damageModifier = damageModifier;
    }

    public Set<CharacterEquippedWeapon> getCharacterEquippedWeapons() {
        return this.characterEquippedWeapons;
    }

    public void setCharacterEquippedWeapons(Set<CharacterEquippedWeapon> characterEquippedWeapons) {
        if (this.characterEquippedWeapons != null) {
            this.characterEquippedWeapons.forEach(i -> i.setWeapon(null));
        }
        if (characterEquippedWeapons != null) {
            characterEquippedWeapons.forEach(i -> i.setWeapon(this));
        }
        this.characterEquippedWeapons = characterEquippedWeapons;
    }

    public Weapon characterEquippedWeapons(Set<CharacterEquippedWeapon> characterEquippedWeapons) {
        this.setCharacterEquippedWeapons(characterEquippedWeapons);
        return this;
    }

    public Weapon addCharacterEquippedWeapon(CharacterEquippedWeapon characterEquippedWeapon) {
        this.characterEquippedWeapons.add(characterEquippedWeapon);
        characterEquippedWeapon.setWeapon(this);
        return this;
    }

    public Weapon removeCharacterEquippedWeapon(CharacterEquippedWeapon characterEquippedWeapon) {
        this.characterEquippedWeapons.remove(characterEquippedWeapon);
        characterEquippedWeapon.setWeapon(null);
        return this;
    }

    public Set<WeaponManeuver> getWeaponManeuvers() {
        return this.weaponManeuvers;
    }

    public void setWeaponManeuvers(Set<WeaponManeuver> weaponManeuvers) {
        if (this.weaponManeuvers != null) {
            this.weaponManeuvers.forEach(i -> i.setWeapon(null));
        }
        if (weaponManeuvers != null) {
            weaponManeuvers.forEach(i -> i.setWeapon(this));
        }
        this.weaponManeuvers = weaponManeuvers;
    }

    public Weapon weaponManeuvers(Set<WeaponManeuver> weaponManeuvers) {
        this.setWeaponManeuvers(weaponManeuvers);
        return this;
    }

    public Weapon addWeaponManeuver(WeaponManeuver weaponManeuver) {
        this.weaponManeuvers.add(weaponManeuver);
        weaponManeuver.setWeapon(this);
        return this;
    }

    public Weapon removeWeaponManeuver(WeaponManeuver weaponManeuver) {
        this.weaponManeuvers.remove(weaponManeuver);
        weaponManeuver.setWeapon(null);
        return this;
    }

    public Character getCharacter() {
        return this.character;
    }

    public void setCharacter(Character character) {
        this.character = character;
        this.characterId = character != null ? character.getId() : null;
    }

    public Weapon character(Character character) {
        this.setCharacter(character);
        return this;
    }

    public Long getCharacterId() {
        return this.characterId;
    }

    public void setCharacterId(Long character) {
        this.characterId = character;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Weapon)) {
            return false;
        }
        return id != null && id.equals(((Weapon) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Weapon{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", weight=" + getWeight() +
            ", quality=" + getQuality() +
            ", picture='" + getPicture() + "'" +
            ", pictureContentType='" + getPictureContentType() + "'" +
            ", reach=" + getReach() +
            ", baseDamage=" + getBaseDamage() +
            ", requiredST=" + getRequiredST() +
            ", damageModifier=" + getDamageModifier() +
            "}";
    }
}
