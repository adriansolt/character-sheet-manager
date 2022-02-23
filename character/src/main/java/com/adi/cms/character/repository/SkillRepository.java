package com.adi.cms.character.repository;

import com.adi.cms.character.domain.Skill;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Skill entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {}
