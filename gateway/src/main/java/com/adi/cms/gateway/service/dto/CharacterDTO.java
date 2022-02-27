package com.adi.cms.gateway.service.dto;

import com.adi.cms.gateway.domain.enumeration.Handedness;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.adi.cms.gateway.domain.Character} entity.
 */
public class CharacterDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String name;

    @NotNull(message = "must not be null")
    private Integer weight;

    @NotNull(message = "must not be null")
    private Integer height;

    @NotNull(message = "must not be null")
    private Integer points;

    @Lob
    private byte[] picture;

    private String pictureContentType;

    @NotNull(message = "must not be null")
    private Handedness handedness;

    @NotNull(message = "must not be null")
    private Boolean active;

    private UserDTO user;

    private CampaignDTO campaign;

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

    public CampaignDTO getCampaign() {
        return campaign;
    }

    public void setCampaign(CampaignDTO campaign) {
        this.campaign = campaign;
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
            ", active='" + getActive() + "'" +
            ", user=" + getUser() +
            ", campaign=" + getCampaign() +
            "}";
    }
}
