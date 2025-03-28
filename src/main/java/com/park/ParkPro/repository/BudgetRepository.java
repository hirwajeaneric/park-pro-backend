package com.park.ParkPro.repository;

import com.park.ParkPro.model.Budget;
import com.park.ParkPro.model.BudgetStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findAllByParkId(UUID parkId);
    List<Budget> findAllByParkIdAndStatus(UUID parkId, BudgetStatus status);
    Optional<Budget> findByParkIdAndFiscalYear(UUID parkId, Integer fiscalYear);

    @Query("SELECT b FROM Budget b WHERE b.fiscalYear = :year AND b.status = 'APPROVED'")
    List<Budget> findAllApprovedBudgetByYear(@Param("year") Integer year);

    @Query("SELECT b FROM Budget b WHERE b.park.id = :parkId AND b.status = 'APPROVED'" + "AND b.fiscalYear BETWEEN :startYaer AND :endYear")
    List<Budget> findApprovedBudgetsByParkAndYearRange(
            @Param("parkId") UUID parkId,
            @Param("startYear") Integer startYear,
            @Param("endYear") Integer endYear
    );

    @Query("SELECT SUM(b.totalAmount) FROM Budget b WHERE b.park.id = :parkId AND b.status = 'APPROVED'")
    Optional<BigDecimal> getTotalApprovedBudgetAmountByPark(@Param("parkId") UUID parkId);
}
