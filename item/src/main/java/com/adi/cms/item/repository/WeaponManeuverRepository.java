package com.adi.cms.item.repository;

import com.adi.cms.item.domain.WeaponManeuver;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WeaponManeuver entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WeaponManeuverRepository extends JpaRepository<WeaponManeuver, Long> {}
