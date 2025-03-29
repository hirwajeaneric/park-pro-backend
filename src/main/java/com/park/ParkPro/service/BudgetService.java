package com.park.ParkPro.service;

import com.park.ParkPro.exception.BusinessRuleException;
import com.park.ParkPro.exception.ResourceNotFoundException;
import com.park.ParkPro.model.*;
import com.park.ParkPro.repository.BudgetCategoryRepository;
import com.park.ParkPro.repository.BudgetRepository;
import com.park.ParkPro.repository.ParkRepository;
import com.park.ParkPro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final BudgetCategoryRepository categoryRepository;
    private final ParkRepository parkRepository;
    private final UserRepository userRepository;

    @Transactional
    public Budget createBudget(Budget budget, UUID parkId, UUID userId) {
        Park park = parkRepository.findById(parkId)
                .orElseThrow(() -> new ResourceNotFoundException("Park not found with ID: "+ parkId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: "+ userId));

        if (budgetRepository.existsByParkIdAndFiscalYear(parkId, budget.getFiscalYear())) {
            throw new BusinessRuleException("Budget already exists for this park and fiscal year");
        }

        budget.setPark(park);
        budget.setCreatedBy(user);
        budget.setStatus(BudgetStatus.DRAFT);
        budget.setBalance(budget.getTotalAmount());

        Budget savedBudget = budgetRepository.save(budget);

        // Create budget categories
        if (budget.getCategories() != null && !budget.getCategories().isEmpty()) {
            for (BudgetCategory category: budget.getCategories()) {
                category.setBudget(savedBudget);
                category.setUsedAmount(BigDecimal.ZERO);
                category.setBalance(category.getAllocatedAmount());
                categoryRepository.save(category);
            }
        }

        log.info("Created budget with ID: {} for park: {}", savedBudget.getId(), park.getName());
        return savedBudget;
    }

    @Transactional(readOnly = true)
    public Budget getBudgetById(UUID id) {
        return budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found with ID: "+ id));
    }

    @Transactional(readOnly = true)
    public List<Budget> getBudgetsByPark(UUID parkId) {
        return budgetRepository.findAllByParkId(parkId);
    }

    @Transactional(readOnly = true)
    public List<Budget> getApprovedBudgetsByYear(Integer year) {
        return budgetRepository.findAllApprovedBudgetsByYear(year);
    }

    @Transactional
    public Budget approveBudget(UUID budgetId, UUID approverId) {
        Budget budget = getBudgetById(budgetId);
        User approver = userRepository.findById(approverId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: "+ approverId));

        if (budget.getStatus() != BudgetStatus.DRAFT) {
            throw new BusinessRuleException("Only DRAFT budgets can be approved");
        }

        budget.approve(approver);
        Budget approvedBudget = budgetRepository.save(budget);

        log.info("Budget {} approved by user {}", budgetId, approverId);
        return approvedBudget;
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalApprovedBudgetAmountByPark(UUID parkId) {
        return budgetRepository.getTotalApprovedBudgetAmountByPark(parkId)
                .orElse(BigDecimal.ZERO);
    }

    @Transactional
    public BudgetCategory addCategoryToBudget(UUID budgetId, BudgetCategory category) {
        Budget budget = getBudgetById(budgetId);

        if (budget.getStatus() != BudgetStatus.DRAFT) {
            throw new BusinessRuleException("Category can only be added to a DRAFT budget");
        }

        category.setBudget(budget);
        category.setUsedAmount(BigDecimal.ZERO);
        category.setBalance(category.getAllocatedAmount());

        BudgetCategory savedCategory = categoryRepository.save(category);

        // Update budget total amount and balance
        budget.setTotalAmount(budget.getTotalAmount().add(category.getAllocatedAmount()));
        budget.setBalance(budget.getBalance().add(category.getAllocatedAmount()));
        budgetRepository.save(budget);

        return savedCategory;
    }
}
