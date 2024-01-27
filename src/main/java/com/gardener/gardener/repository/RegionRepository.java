package com.gardener.gardener.repository;

import com.gardener.gardener.entity.PlantCulture;
import com.gardener.gardener.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    List<Region> findByParentRegionId(Long parentId);

    List<Region> findByParentRegionIdIsNull();
}