package com.adi.cms.gateway.service.dto;

import com.adi.cms.gateway.domain.enumeration.AttributeName;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.adi.cms.gateway.domain.XaracterAttribute} entity.
 */
public class XaracterAttributeDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private AttributeName name;

    @NotNull(message = "must not be null")
    private Integer points;

    private Integer attributeModifier;

    private XaracterDTO xaracterId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AttributeName getName() {
        return name;
    }

    public void setName(AttributeName name) {
        this.name = name;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getAttributeModifier() {
        return attributeModifier;
    }

    public void setAttributeModifier(Integer attributeModifier) {
        this.attributeModifier = attributeModifier;
    }

    public XaracterDTO getXaracterId() {
        return xaracterId;
    }

    public void setXaracterId(XaracterDTO xaracterId) {
        this.xaracterId = xaracterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof XaracterAttributeDTO)) {
            return false;
        }

        XaracterAttributeDTO xaracterAttributeDTO = (XaracterAttributeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, xaracterAttributeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "XaracterAttributeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", points=" + getPoints() +
            ", attributeModifier=" + getAttributeModifier() +
            ", xaracterId=" + getXaracterId() +
            "}";
    }
}
