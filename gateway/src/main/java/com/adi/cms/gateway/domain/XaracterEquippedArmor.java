package com.adi.cms.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A XaracterEquippedArmor.
 */
@Table("xaracter_equipped_armor")
public class XaracterEquippedArmor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("xaracter_id")
    private Long xaracterId;

    @Transient
    @JsonIgnoreProperties(value = { "xaracterEquippedArmors" }, allowSetters = true)
    private ArmorPiece armorPiece;

    @Column("armor_piece_id")
    private Long armorPieceId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public XaracterEquippedArmor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getXaracterId() {
        return this.xaracterId;
    }

    public XaracterEquippedArmor xaracterId(Long xaracterId) {
        this.setXaracterId(xaracterId);
        return this;
    }

    public void setXaracterId(Long xaracterId) {
        this.xaracterId = xaracterId;
    }

    public ArmorPiece getArmorPiece() {
        return this.armorPiece;
    }

    public void setArmorPiece(ArmorPiece armorPiece) {
        this.armorPiece = armorPiece;
        this.armorPieceId = armorPiece != null ? armorPiece.getId() : null;
    }

    public XaracterEquippedArmor armorPiece(ArmorPiece armorPiece) {
        this.setArmorPiece(armorPiece);
        return this;
    }

    public Long getArmorPieceId() {
        return this.armorPieceId;
    }

    public void setArmorPieceId(Long armorPiece) {
        this.armorPieceId = armorPiece;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof XaracterEquippedArmor)) {
            return false;
        }
        return id != null && id.equals(((XaracterEquippedArmor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "XaracterEquippedArmor{" +
            "id=" + getId() +
            ", xaracterId=" + getXaracterId() +
            "}";
    }
}
