package com.gardener.gardener.repository;

import com.gardener.gardener.entity.WorkProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkProgressRepository extends JpaRepository<WorkProgress, Long> {
}
