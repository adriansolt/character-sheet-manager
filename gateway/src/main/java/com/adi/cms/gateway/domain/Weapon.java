package com.adi.cms.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Weapon.
 */
@Table("weapon")
public class Weapon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

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
    @JsonIgnoreProperties(value = { "weaponId" }, allowSetters = true)
    private Set<XaracterEquippedWeapon> xaracterEquippedWeapons = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "weaponId", "maneuverId" }, allowSetters = true)
    private Set<WeaponManeuver> weaponManeuvers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Weapon id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<XaracterEquippedWeapon> getXaracterEquippedWeapons() {
        return this.xaracterEquippedWeapons;
    }

    public void setXaracterEquippedWeapons(Set<XaracterEquippedWeapon> xaracterEquippedWeapons) {
        if (this.xaracterEquippedWeapons != null) {
            this.xaracterEquippedWeapons.forEach(i -> i.setWeaponId(null));
        }
        if (xaracterEquippedWeapons != null) {
            xaracterEquippedWeapons.forEach(i -> i.setWeaponId(this));
        }
        this.xaracterEquippedWeapons = xaracterEquippedWeapons;
    }

    public Weapon xaracterEquippedWeapons(Set<XaracterEquippedWeapon> xaracterEquippedWeapons) {
        this.setXaracterEquippedWeapons(xaracterEquippedWeapons);
        return this;
    }

    public Weapon addXaracterEquippedWeapon(XaracterEquippedWeapon xaracterEquippedWeapon) {
        this.xaracterEquippedWeapons.add(xaracterEquippedWeapon);
        xaracterEquippedWeapon.setWeaponId(this);
        return this;
    }

    public Weapon removeXaracterEquippedWeapon(XaracterEquippedWeapon xaracterEquippedWeapon) {
        this.xaracterEquippedWeapons.remove(xaracterEquippedWeapon);
        xaracterEquippedWeapon.setWeaponId(null);
        return this;
    }

    public Set<WeaponManeuver> getWeaponManeuvers() {
        return this.weaponManeuvers;
    }

    public void setWeaponManeuvers(Set<WeaponManeuver> weaponManeuvers) {
        if (this.weaponManeuvers != null) {
            this.weaponManeuvers.forEach(i -> i.setWeaponId(null));
        }
        if (weaponManeuvers != null) {
            weaponManeuvers.forEach(i -> i.setWeaponId(this));
        }
        this.weaponManeuvers = weaponManeuvers;
    }

    public Weapon weaponManeuvers(Set<WeaponManeuver> weaponManeuvers) {
        this.setWeaponManeuvers(weaponManeuvers);
        return this;
    }

    public Weapon addWeaponManeuver(WeaponManeuver weaponManeuver) {
        this.weaponManeuvers.add(weaponManeuver);
        weaponManeuver.setWeaponId(this);
        return this;
    }

    public Weapon removeWeaponManeuver(WeaponManeuver weaponManeuver) {
        this.weaponManeuvers.remove(weaponManeuver);
        weaponManeuver.setWeaponId(null);
        return this;
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
            ", reach=" + getReach() +
            ", baseDamage=" + getBaseDamage() +
            ", requiredST=" + getRequiredST() +
            ", damageModifier=" + getDamageModifier() +
            "}";
    }
}
