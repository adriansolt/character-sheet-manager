package com.adi.cms.character.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.adi.cms.character.domain.CharacterSkill} entity.
 */
public class CharacterSkillDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer points;

    private Integer skillModifier;

    private CharacterDTO characterId;

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

    public CharacterDTO getCharacterId() {
        return characterId;
    }

    public void setCharacterId(CharacterDTO characterId) {
        this.characterId = characterId;
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
        if (!(o instanceof CharacterSkillDTO)) {
            return false;
        }

        CharacterSkillDTO characterSkillDTO = (CharacterSkillDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, characterSkillDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CharacterSkillDTO{" +
            "id=" + getId() +
            ", points=" + getPoints() +
            ", skillModifier=" + getSkillModifier() +
            ", characterId=" + getCharacterId() +
            ", skillId=" + getSkillId() +
            "}";
    }
}
