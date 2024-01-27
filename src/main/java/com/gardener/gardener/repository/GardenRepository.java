package com.gardener.gardener.repository;

import com.gardener.gardener.entity.Garden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GardenRepository extends JpaRepository<Garden, Long> {

}