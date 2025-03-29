package com.park.ParkPro.controller;

import com.park.ParkPro.model.Budget;
import com.park.ParkPro.model.BudgetCategory;
import com.park.ParkPro.service.BudgetService;
//import com.sun.security.auth.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
@Tag(name = "Budget Management", description = "Endpoints for managing park budgets")
@SecurityRequirement(name = "bearerAuth")
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping("/park/{parkId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_OFFICER')")
    @Operation(summary = "Create a new budget for a park")
    public ResponseEntity<Budget> createBudget(
            @PathVariable UUID parkId,
            @Valid @RequestBody Budget budget,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Budget createdBudget = budgetService.createBudget(budget, parkId, userPrincipal.getId());
        return ResponseEntity
                .created(URI.create("/api/budgets/" + createdBudget.getId()))
                .body(createdBudget);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_OFFICER', 'PARK_MANAGER')")
    @Operation(summary = "Get budget by ID")
    public ResponseEntity<Budget> getBudgetById(@PathVariable UUID id) {
        return ResponseEntity.ok(budgetService.getBudgetById(id));
    }

    @GetMapping("/park/{parkId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_OFFICER', 'PARK_MANAGER')")
    @Operation(summary = "Get all budgets for a park")
    public ResponseEntity<List<Budget>> getBudgetsByPark(@PathVariable UUID parkId) {
        return ResponseEntity.ok(budgetService.getBudgetsByPark(parkId));
    }

    @GetMapping("/year/{year}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_OFFICER')")
    @Operation(summary = "Get approved budgets by year")
    public ResponseEntity<List<Budget>> getApprovedBudgetsByYear(@PathVariable Integer year) {
        return ResponseEntity.ok(budgetService.getApprovedBudgetsByYear(year));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_OFFICER')")
    @Operation(summary = "Approve a budget")
    public ResponseEntity<Budget> approveBudget(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(budgetService.approveBudget(id, userPrincipal.getId()));
    }

    @GetMapping("/park/{parkId}/total")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_OFFICER', 'PARK_MANAGER')")
    @Operation(summary = "Get total approved budget amount for a park")
    public ResponseEntity<BigDecimal> getTotalApprovedBudgetAmountByPark(@PathVariable UUID parkId) {
        return ResponseEntity.ok(budgetService.getTotalApprovedBudgetAmountByPark(parkId));
    }

    @PostMapping("/{budgetId}/categories")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_OFFICER')")
    @Operation(summary = "Add a category to a budget")
    public ResponseEntity<BudgetCategory> addCategoryToBudget(
            @PathVariable UUID budgetId,
            @Valid @RequestBody BudgetCategory category) {
        return ResponseEntity.ok(budgetService.addCategoryToBudget(budgetId, category));
    }
}
