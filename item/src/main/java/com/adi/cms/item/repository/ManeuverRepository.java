package com.adi.cms.item.repository;

import com.adi.cms.item.domain.Maneuver;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Maneuver entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ManeuverRepository extends JpaRepository<Maneuver, Long> {}
