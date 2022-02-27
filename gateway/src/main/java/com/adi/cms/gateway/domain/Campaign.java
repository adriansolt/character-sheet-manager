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
 * A Campaign.
 */
@Table("campaign")
public class Campaign implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("map")
    private byte[] map;

    @NotNull
    @Column("map_content_type")
    private String mapContentType;

    @NotNull(message = "must not be null")
    @Column("master_id")
    private Long masterId;

    @Transient
    @JsonIgnoreProperties(
        value = { "items", "weapons", "armorPieces", "notes", "characterAttributes", "characterSkills", "user", "campaign" },
        allowSetters = true
    )
    private Set<Character> characters = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "campaign", "character" }, allowSetters = true)
    private Set<Item> items = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "characterEquippedWeapons", "weaponManeuvers", "campaign", "character" }, allowSetters = true)
    private Set<Weapon> weapons = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "characterEquippedArmors", "campaign", "character" }, allowSetters = true)
    private Set<ArmorPiece> armorPieces = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "campaign", "user" }, allowSetters = true)
    private Set<CampaignUser> campaignUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Campaign id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Campaign name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Campaign description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getMap() {
        return this.map;
    }

    public Campaign map(byte[] map) {
        this.setMap(map);
        return this;
    }

    public void setMap(byte[] map) {
        this.map = map;
    }

    public String getMapContentType() {
        return this.mapContentType;
    }

    public Campaign mapContentType(String mapContentType) {
        this.mapContentType = mapContentType;
        return this;
    }

    public void setMapContentType(String mapContentType) {
        this.mapContentType = mapContentType;
    }

    public Long getMasterId() {
        return this.masterId;
    }

    public Campaign masterId(Long masterId) {
        this.setMasterId(masterId);
        return this;
    }

    public void setMasterId(Long masterId) {
        this.masterId = masterId;
    }

    public Set<Character> getCharacters() {
        return this.characters;
    }

    public void setCharacters(Set<Character> characters) {
        if (this.characters != null) {
            this.characters.forEach(i -> i.setCampaign(null));
        }
        if (characters != null) {
            characters.forEach(i -> i.setCampaign(this));
        }
        this.characters = characters;
    }

    public Campaign characters(Set<Character> characters) {
        this.setCharacters(characters);
        return this;
    }

    public Campaign addCharacter(Character character) {
        this.characters.add(character);
        character.setCampaign(this);
        return this;
    }

    public Campaign removeCharacter(Character character) {
        this.characters.remove(character);
        character.setCampaign(null);
        return this;
    }

    public Set<Item> getItems() {
        return this.items;
    }

    public void setItems(Set<Item> items) {
        if (this.items != null) {
            this.items.forEach(i -> i.setCampaign(null));
        }
        if (items != null) {
            items.forEach(i -> i.setCampaign(this));
        }
        this.items = items;
    }

    public Campaign items(Set<Item> items) {
        this.setItems(items);
        return this;
    }

    public Campaign addItem(Item item) {
        this.items.add(item);
        item.setCampaign(this);
        return this;
    }

    public Campaign removeItem(Item item) {
        this.items.remove(item);
        item.setCampaign(null);
        return this;
    }

    public Set<Weapon> getWeapons() {
        return this.weapons;
    }

    public void setWeapons(Set<Weapon> weapons) {
        if (this.weapons != null) {
            this.weapons.forEach(i -> i.setCampaign(null));
        }
        if (weapons != null) {
            weapons.forEach(i -> i.setCampaign(this));
        }
        this.weapons = weapons;
    }

    public Campaign weapons(Set<Weapon> weapons) {
        this.setWeapons(weapons);
        return this;
    }

    public Campaign addWeapon(Weapon weapon) {
        this.weapons.add(weapon);
        weapon.setCampaign(this);
        return this;
    }

    public Campaign removeWeapon(Weapon weapon) {
        this.weapons.remove(weapon);
        weapon.setCampaign(null);
        return this;
    }

    public Set<ArmorPiece> getArmorPieces() {
        return this.armorPieces;
    }

    public void setArmorPieces(Set<ArmorPiece> armorPieces) {
        if (this.armorPieces != null) {
            this.armorPieces.forEach(i -> i.setCampaign(null));
        }
        if (armorPieces != null) {
            armorPieces.forEach(i -> i.setCampaign(this));
        }
        this.armorPieces = armorPieces;
    }

    public Campaign armorPieces(Set<ArmorPiece> armorPieces) {
        this.setArmorPieces(armorPieces);
        return this;
    }

    public Campaign addArmorPiece(ArmorPiece armorPiece) {
        this.armorPieces.add(armorPiece);
        armorPiece.setCampaign(this);
        return this;
    }

    public Campaign removeArmorPiece(ArmorPiece armorPiece) {
        this.armorPieces.remove(armorPiece);
        armorPiece.setCampaign(null);
        return this;
    }

    public Set<CampaignUser> getCampaignUsers() {
        return this.campaignUsers;
    }

    public void setCampaignUsers(Set<CampaignUser> campaignUsers) {
        if (this.campaignUsers != null) {
            this.campaignUsers.forEach(i -> i.setCampaign(null));
        }
        if (campaignUsers != null) {
            campaignUsers.forEach(i -> i.setCampaign(this));
        }
        this.campaignUsers = campaignUsers;
    }

    public Campaign campaignUsers(Set<CampaignUser> campaignUsers) {
        this.setCampaignUsers(campaignUsers);
        return this;
    }

    public Campaign addCampaignUser(CampaignUser campaignUser) {
        this.campaignUsers.add(campaignUser);
        campaignUser.setCampaign(this);
        return this;
    }

    public Campaign removeCampaignUser(CampaignUser campaignUser) {
        this.campaignUsers.remove(campaignUser);
        campaignUser.setCampaign(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Campaign)) {
            return false;
        }
        return id != null && id.equals(((Campaign) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Campaign{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", map='" + getMap() + "'" +
            ", mapContentType='" + getMapContentType() + "'" +
            ", masterId=" + getMasterId() +
            "}";
    }
}
