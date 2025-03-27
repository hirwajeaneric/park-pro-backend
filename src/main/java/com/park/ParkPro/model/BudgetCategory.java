package com.park.ParkPro.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "budget_category")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class BudgetCategory extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id", nullable = false)
    @ToString.Exclude
    private Budget budget;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal allocatedAmount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal usedAmount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    @OneToMany(mappedBy = "budgetCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Expense> expenses = new HashSet<>();

    public void addExpense(Expense expense) {
        this.usedAmount = this.usedAmount.add(expense.getAmount());
        this.balance = this.allocatedAmount.subtract(this.usedAmount);
        this.expenses.add(expense);
        expense.setBudgetCategory(this);
    }
}
