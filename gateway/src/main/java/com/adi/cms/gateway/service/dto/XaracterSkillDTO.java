package com.adi.cms.gateway.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.adi.cms.gateway.domain.XaracterSkill} entity.
 */
public class XaracterSkillDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Integer points;

    private Integer skillModifier;

    private XaracterDTO xaracterId;

    private SkillDTO skillId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getSkillModifier() {
        return skillModifier;
    }

    public void setSkillModifier(Integer skillModifier) {
        this.skillModifier = skillModifier;
    }

    public XaracterDTO getXaracterId() {
        return xaracterId;
    }

    public void setXaracterId(XaracterDTO xaracterId) {
        this.xaracterId = xaracterId;
    }

    public SkillDTO getSkillId() {
        return skillId;
    }

    public void setSkillId(SkillDTO skillId) {
        this.skillId = skillId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof XaracterSkillDTO)) {
            return false;
        }

        XaracterSkillDTO xaracterSkillDTO = (XaracterSkillDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, xaracterSkillDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "XaracterSkillDTO{" +
            "id=" + getId() +
            ", points=" + getPoints() +
            ", skillModifier=" + getSkillModifier() +
            ", xaracterId=" + getXaracterId() +
            ", skillId=" + getSkillId() +
            "}";
    }
}
