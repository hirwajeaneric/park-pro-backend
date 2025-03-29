package com.park.ParkPro.repository;

import com.park.ParkPro.model.BudgetCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BudgetCategoryRepository extends JpaRepository<BudgetCategory, UUID> {
    List<BudgetCategory> findAllByBudgetId(UUID budgetId);
}
