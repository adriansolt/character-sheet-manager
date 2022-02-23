package com.adi.cms.item.domain;

import com.adi.cms.item.domain.enumeration.Handedness;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A XaracterEquippedWeapon.
 */
@Entity
@Table(name = "xaracter_equipped_weapon")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class XaracterEquippedWeapon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "xaracter_id")
    private Long xaracterId;

    @Enumerated(EnumType.STRING)
    @Column(name = "hand")
    private Handedness hand;

    @ManyToOne
    @JsonIgnoreProperties(value = { "xaracterEquippedWeapons", "weaponManeuvers" }, allowSetters = true)
    private Weapon weaponId;

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
    }

    public XaracterEquippedWeapon weaponId(Weapon weapon) {
        this.setWeaponId(weapon);
        return this;
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
