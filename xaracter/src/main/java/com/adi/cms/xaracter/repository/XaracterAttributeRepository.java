package com.adi.cms.xaracter.repository;

import com.adi.cms.xaracter.domain.XaracterAttribute;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the XaracterAttribute entity.
 */
@SuppressWarnings("unused")
@Repository
public interface XaracterAttributeRepository extends JpaRepository<XaracterAttribute, Long> {}
