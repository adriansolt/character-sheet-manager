package com.adi.cms.item.repository;

import com.adi.cms.item.domain.CharacterEquippedWeapon;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CharacterEquippedWeapon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CharacterEquippedWeaponRepository extends JpaRepository<CharacterEquippedWeapon, Long> {}
