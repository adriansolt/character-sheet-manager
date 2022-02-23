package com.adi.cms.item.repository;

import com.adi.cms.item.domain.XaracterEquippedArmor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the XaracterEquippedArmor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface XaracterEquippedArmorRepository extends JpaRepository<XaracterEquippedArmor, Long> {}
