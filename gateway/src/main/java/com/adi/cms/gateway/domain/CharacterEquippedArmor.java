package com.adi.cms.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A CharacterEquippedArmor.
 */
@Table("character_equipped_armor")
public class CharacterEquippedArmor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("character_id")
    private Long characterId;

    @Transient
    @JsonIgnoreProperties(value = { "characterEquippedArmors" }, allowSetters = true)
    private ArmorPiece armorPiece;

    @Column("armor_piece_id")
    private Long armorPieceId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CharacterEquippedArmor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCharacterId() {
        return this.characterId;
    }

    public CharacterEquippedArmor characterId(Long characterId) {
        this.setCharacterId(characterId);
        return this;
    }

    public void setCharacterId(Long characterId) {
        this.characterId = characterId;
    }

    public ArmorPiece getArmorPiece() {
        return this.armorPiece;
    }

    public void setArmorPiece(ArmorPiece armorPiece) {
        this.armorPiece = armorPiece;
        this.armorPieceId = armorPiece != null ? armorPiece.getId() : null;
    }

    public CharacterEquippedArmor armorPiece(ArmorPiece armorPiece) {
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
        if (!(o instanceof CharacterEquippedArmor)) {
            return false;
        }
        return id != null && id.equals(((CharacterEquippedArmor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CharacterEquippedArmor{" +
            "id=" + getId() +
            ", characterId=" + getCharacterId() +
            "}";
    }
}
