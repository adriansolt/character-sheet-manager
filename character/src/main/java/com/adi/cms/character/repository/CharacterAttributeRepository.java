package com.adi.cms.character.repository;

import com.adi.cms.character.domain.CharacterAttribute;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CharacterAttribute entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CharacterAttributeRepository extends JpaRepository<CharacterAttribute, Long> {}
