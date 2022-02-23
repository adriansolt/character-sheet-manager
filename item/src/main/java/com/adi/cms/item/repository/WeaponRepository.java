package com.adi.cms.item.repository;

import com.adi.cms.item.domain.Weapon;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Weapon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WeaponRepository extends JpaRepository<Weapon, Long> {}
