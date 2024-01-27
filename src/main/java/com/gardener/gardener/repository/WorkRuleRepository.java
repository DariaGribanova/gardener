package com.gardener.gardener.repository;

import com.gardener.gardener.entity.WorkRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkRuleRepository extends JpaRepository<WorkRule, Long> {
}
