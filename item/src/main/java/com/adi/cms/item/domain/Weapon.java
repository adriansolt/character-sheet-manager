package com.adi.cms.item.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Weapon.
 */
@Entity
@Table(name = "weapon")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Weapon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "reach", nullable = false)
    private Integer reach;

    @NotNull
    @Min(value = 0)
    @Column(name = "base_damage", nullable = false)
    private Integer baseDamage;

    @NotNull
    @Min(value = 1)
    @Column(name = "required_st", nullable = false)
    private Integer requiredST;

    @Column(name = "damage_modifier")
    private Integer damageModifier;

    @OneToMany(mappedBy = "weaponId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "weaponId" }, allowSetters = true)
    private Set<XaracterEquippedWeapon> xaracterEquippedWeapons = new HashSet<>();

    @OneToMany(mappedBy = "weaponId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
