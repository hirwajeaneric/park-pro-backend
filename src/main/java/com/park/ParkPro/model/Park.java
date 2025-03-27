package com.park.ParkPro.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "park")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Park extends BaseEntity {
    @Column(nullable = false, length = 100)
    private String name;

    @Column
    private String location;

    private String description;

    @OneToMany(mappedBy = "park", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "park", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Budget> budgets = new HashSet<>();

    @OneToMany(mappedBy = "park", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Expense> expenses = new HashSet<>();

//    @OneToMany(mappedBy = "park", cascade = CascadeType.ALL, orphanRemoval = true)
//    @ToString.Exclude
//    private Set<Bookings> bookings = new HashSet<>();
//
//    @OneToMany(mappedBy = "park", cascade = CascadeType.ALL, orphanRemoval = true)
//    @ToString.Exclude
//    private Set<Donation> donations = new HashSet<>();
}
