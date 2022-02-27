package com.adi.cms.gateway.domain;

import com.adi.cms.gateway.domain.enumeration.Handedness;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A CharacterEquippedWeapon.
 */
@Table("character_equipped_weapon")
public class CharacterEquippedWeapon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("hand")
    private Handedness hand;

    @Transient
    @JsonIgnoreProperties(value = { "characterEquippedWeapons", "weaponManeuvers", "character" }, allowSetters = true)
    private Weapon weapon;

    @Column("weapon_id")
    private Long weaponId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CharacterEquippedWeapon id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Handedness getHand() {
        return this.hand;
    }

    public CharacterEquippedWeapon hand(Handedness hand) {
        this.setHand(hand);
        return this;
    }

    public void setHand(Handedness hand) {
        this.hand = hand;
    }

    public Weapon getWeapon() {
        return this.weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
        this.weaponId = weapon != null ? weapon.getId() : null;
    }

    public CharacterEquippedWeapon weapon(Weapon weapon) {
        this.setWeapon(weapon);
        return this;
    }

    public Long getWeaponId() {
        return this.weaponId;
    }

    public void setWeaponId(Long weapon) {
        this.weaponId = weapon;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CharacterEquippedWeapon)) {
            return false;
        }
        return id != null && id.equals(((CharacterEquippedWeapon) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CharacterEquippedWeapon{" +
            "id=" + getId() +
            ", hand='" + getHand() + "'" +
            "}";
    }
}
