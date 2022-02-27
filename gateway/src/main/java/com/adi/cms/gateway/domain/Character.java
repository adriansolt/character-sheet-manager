package com.adi.cms.gateway.domain;

import com.adi.cms.gateway.domain.enumeration.Handedness;
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
 * A Character.
 */
@Table("character")
public class Character implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @NotNull(message = "must not be null")
    @Column("weight")
    private Integer weight;

    @NotNull(message = "must not be null")
    @Column("height")
    private Integer height;

    @NotNull(message = "must not be null")
    @Column("points")
    private Integer points;

    @Column("picture")
    private byte[] picture;

    @Column("picture_content_type")
    private String pictureContentType;

    @Column("handedness")
    private Handedness handedness;

    @Column("active")
    private Boolean active;

    @Transient
    @JsonIgnoreProperties(value = { "character" }, allowSetters = true)
    private Set<Note> notes = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "character" }, allowSetters = true)
    private Set<CharacterAttribute> characterAttributes = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "character", "skill" }, allowSetters = true)
    private Set<CharacterSkill> characterSkills = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "character" }, allowSetters = true)
    private Set<Item> items = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "characterEquippedWeapons", "weaponManeuvers", "character" }, allowSetters = true)
    private Set<Weapon> weapons = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "characterEquippedArmors", "character" }, allowSetters = true)
    private Set<ArmorPiece> armorPieces = new HashSet<>();

    @Transient
    private User user;

    @Transient
    @JsonIgnoreProperties(value = { "campaignUsers", "characters" }, allowSetters = true)
    private Campaign campaign;

    @Column("user_id")
    private String userId;

    @Column("campaign_id")
    private Long campaignId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Character id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Character name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public Character weight(Integer weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return this.height;
    }

    public Character height(Integer height) {
        this.setHeight(height);
        return this;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getPoints() {
        return this.points;
    }

    public Character points(Integer points) {
        this.setPoints(points);
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public byte[] getPicture() {
        return this.picture;
    }

    public Character picture(byte[] picture) {
        this.setPicture(picture);
        return this;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return this.pictureContentType;
    }

    public Character pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public Handedness getHandedness() {
        return this.handedness;
    }

    public Character handedness(Handedness handedness) {
        this.setHandedness(handedness);
        return this;
    }

    public void setHandedness(Handedness handedness) {
        this.handedness = handedness;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Character active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Note> getNotes() {
        return this.notes;
    }

    public void setNotes(Set<Note> notes) {
        if (this.notes != null) {
            this.notes.forEach(i -> i.setCharacter(null));
        }
        if (notes != null) {
            notes.forEach(i -> i.setCharacter(this));
        }
        this.notes = notes;
    }

    public Character notes(Set<Note> notes) {
        this.setNotes(notes);
        return this;
    }

    public Character addNote(Note note) {
        this.notes.add(note);
        note.setCharacter(this);
        return this;
    }

    public Character removeNote(Note note) {
        this.notes.remove(note);
        note.setCharacter(null);
        return this;
    }

    public Set<CharacterAttribute> getCharacterAttributes() {
        return this.characterAttributes;
    }

    public void setCharacterAttributes(Set<CharacterAttribute> characterAttributes) {
        if (this.characterAttributes != null) {
            this.characterAttributes.forEach(i -> i.setCharacter(null));
        }
        if (characterAttributes != null) {
            characterAttributes.forEach(i -> i.setCharacter(this));
        }
        this.characterAttributes = characterAttributes;
    }

    public Character characterAttributes(Set<CharacterAttribute> characterAttributes) {
        this.setCharacterAttributes(characterAttributes);
        return this;
    }

    public Character addCharacterAttribute(CharacterAttribute characterAttribute) {
        this.characterAttributes.add(characterAttribute);
        characterAttribute.setCharacter(this);
        return this;
    }

    public Character removeCharacterAttribute(CharacterAttribute characterAttribute) {
        this.characterAttributes.remove(characterAttribute);
        characterAttribute.setCharacter(null);
        return this;
    }

    public Set<CharacterSkill> getCharacterSkills() {
        return this.characterSkills;
    }

    public void setCharacterSkills(Set<CharacterSkill> characterSkills) {
        if (this.characterSkills != null) {
            this.characterSkills.forEach(i -> i.setCharacter(null));
        }
        if (characterSkills != null) {
            characterSkills.forEach(i -> i.setCharacter(this));
        }
        this.characterSkills = characterSkills;
    }

    public Character characterSkills(Set<CharacterSkill> characterSkills) {
        this.setCharacterSkills(characterSkills);
        return this;
    }

    public Character addCharacterSkill(CharacterSkill characterSkill) {
        this.characterSkills.add(characterSkill);
        characterSkill.setCharacter(this);
        return this;
    }

    public Character removeCharacterSkill(CharacterSkill characterSkill) {
        this.characterSkills.remove(characterSkill);
        characterSkill.setCharacter(null);
        return this;
    }

    public Set<Item> getItems() {
        return this.items;
    }

    public void setItems(Set<Item> items) {
        if (this.items != null) {
            this.items.forEach(i -> i.setCharacter(null));
        }
        if (items != null) {
            items.forEach(i -> i.setCharacter(this));
        }
        this.items = items;
    }

    public Character items(Set<Item> items) {
        this.setItems(items);
        return this;
    }

    public Character addItem(Item item) {
        this.items.add(item);
        item.setCharacter(this);
        return this;
    }

    public Character removeItem(Item item) {
        this.items.remove(item);
        item.setCharacter(null);
        return this;
    }

    public Set<Weapon> getWeapons() {
        return this.weapons;
    }

    public void setWeapons(Set<Weapon> weapons) {
        if (this.weapons != null) {
            this.weapons.forEach(i -> i.setCharacter(null));
        }
        if (weapons != null) {
            weapons.forEach(i -> i.setCharacter(this));
        }
        this.weapons = weapons;
    }

    public Character weapons(Set<Weapon> weapons) {
        this.setWeapons(weapons);
        return this;
    }

    public Character addWeapon(Weapon weapon) {
        this.weapons.add(weapon);
        weapon.setCharacter(this);
        return this;
    }

    public Character removeWeapon(Weapon weapon) {
        this.weapons.remove(weapon);
        weapon.setCharacter(null);
        return this;
    }

    public Set<ArmorPiece> getArmorPieces() {
        return this.armorPieces;
    }

    public void setArmorPieces(Set<ArmorPiece> armorPieces) {
        if (this.armorPieces != null) {
            this.armorPieces.forEach(i -> i.setCharacter(null));
        }
        if (armorPieces != null) {
            armorPieces.forEach(i -> i.setCharacter(this));
        }
        this.armorPieces = armorPieces;
    }

    public Character armorPieces(Set<ArmorPiece> armorPieces) {
        this.setArmorPieces(armorPieces);
        return this;
    }

    public Character addArmorPiece(ArmorPiece armorPiece) {
        this.armorPieces.add(armorPiece);
        armorPiece.setCharacter(this);
        return this;
    }

    public Character removeArmorPiece(ArmorPiece armorPiece) {
        this.armorPieces.remove(armorPiece);
        armorPiece.setCharacter(null);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user != null ? user.getId() : null;
    }

    public Character user(User user) {
        this.setUser(user);
        return this;
    }

    public Campaign getCampaign() {
        return this.campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
        this.campaignId = campaign != null ? campaign.getId() : null;
    }

    public Character campaign(Campaign campaign) {
        this.setCampaign(campaign);
        return this;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String user) {
        this.userId = user;
    }

    public Long getCampaignId() {
        return this.campaignId;
    }

    public void setCampaignId(Long campaign) {
        this.campaignId = campaign;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Character)) {
            return false;
        }
        return id != null && id.equals(((Character) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Character{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", weight=" + getWeight() +
            ", height=" + getHeight() +
            ", points=" + getPoints() +
            ", picture='" + getPicture() + "'" +
            ", pictureContentType='" + getPictureContentType() + "'" +
            ", handedness='" + getHandedness() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
