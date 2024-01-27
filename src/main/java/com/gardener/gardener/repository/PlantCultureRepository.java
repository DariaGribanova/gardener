package com.gardener.gardener.repository;

import com.gardener.gardener.entity.PlantCulture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantCultureRepository extends JpaRepository<PlantCulture, Long>{
    List<PlantCulture> findByDisplayRootIsTrueAndParentCultureIsNull();

    List<PlantCulture> findByParentCultureId(Long id);
}
