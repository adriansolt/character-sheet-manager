package com.adi.cms.item.repository;

import com.adi.cms.item.domain.CharacterEquippedArmor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CharacterEquippedArmor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CharacterEquippedArmorRepository extends JpaRepository<CharacterEquippedArmor, Long> {}
