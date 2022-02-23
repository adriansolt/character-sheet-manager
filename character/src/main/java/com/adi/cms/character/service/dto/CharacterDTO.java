package com.adi.cms.character.service.dto;

import com.adi.cms.character.domain.enumeration.Handedness;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.adi.cms.character.domain.Character} entity.
 */
public class CharacterDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer weight;

    @NotNull
    private Integer height;

    @NotNull
    private Integer points;

    @Lob
    private byte[] picture;

    private String pictureContentType;
    private Handedness handedness;

    private Long campaignId;

    private Boolean active;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return pictureContentType;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public Handedness getHandedness() {
        return handedness;
    }

    public void setHandedness(Handedness handedness) {
        this.handedness = handedness;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CharacterDTO)) {
            return false;
        }

        CharacterDTO characterDTO = (CharacterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, characterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CharacterDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", weight=" + getWeight() +
            ", height=" + getHeight() +
            ", points=" + getPoints() +
            ", picture='" + getPicture() + "'" +
            ", handedness='" + getHandedness() + "'" +
            ", campaignId=" + getCampaignId() +
            ", active='" + getActive() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
