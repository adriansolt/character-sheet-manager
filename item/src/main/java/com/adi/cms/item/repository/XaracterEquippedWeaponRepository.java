package com.adi.cms.item.repository;

import com.adi.cms.item.domain.XaracterEquippedWeapon;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the XaracterEquippedWeapon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface XaracterEquippedWeaponRepository extends JpaRepository<XaracterEquippedWeapon, Long> {}
