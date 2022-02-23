package com.adi.cms.gateway.domain;

import com.adi.cms.gateway.domain.enumeration.Handedness;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A XaracterEquippedWeapon.
 */
@Table("xaracter_equipped_weapon")
public class XaracterEquippedWeapon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("xaracter_id")
    private Long xaracterId;

    @Column("hand")
    private Handedness hand;

    @Transient
    @JsonIgnoreProperties(value = { "xaracterEquippedWeapons", "weaponManeuvers" }, allowSetters = true)
    private Weapon weaponId;

    @Column("weapon_id_id")
    private Long weaponIdId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public XaracterEquippedWeapon id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getXaracterId() {
        return this.xaracterId;
    }

    public XaracterEquippedWeapon xaracterId(Long xaracterId) {
        this.setXaracterId(xaracterId);
        return this;
    }

    public void setXaracterId(Long xaracterId) {
        this.xaracterId = xaracterId;
    }

    public Handedness getHand() {
        return this.hand;
    }

    public XaracterEquippedWeapon hand(Handedness hand) {
        this.setHand(hand);
        return this;
    }

    public void setHand(Handedness hand) {
        this.hand = hand;
    }

    public Weapon getWeaponId() {
        return this.weaponId;
    }

    public void setWeaponId(Weapon weapon) {
        this.weaponId = weapon;
        this.weaponIdId = weapon != null ? weapon.getId() : null;
    }

    public XaracterEquippedWeapon weaponId(Weapon weapon) {
        this.setWeaponId(weapon);
        return this;
    }

    public Long getWeaponIdId() {
        return this.weaponIdId;
    }

    public void setWeaponIdId(Long weapon) {
        this.weaponIdId = weapon;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof XaracterEquippedWeapon)) {
            return false;
        }
        return id != null && id.equals(((XaracterEquippedWeapon) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "XaracterEquippedWeapon{" +
            "id=" + getId() +
            ", xaracterId=" + getXaracterId() +
            ", hand='" + getHand() + "'" +
            "}";
    }
}
