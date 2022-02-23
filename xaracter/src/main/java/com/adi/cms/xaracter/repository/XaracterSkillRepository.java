package com.adi.cms.xaracter.repository;

import com.adi.cms.xaracter.domain.XaracterSkill;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the XaracterSkill entity.
 */
@SuppressWarnings("unused")
@Repository
public interface XaracterSkillRepository extends JpaRepository<XaracterSkill, Long> {}
